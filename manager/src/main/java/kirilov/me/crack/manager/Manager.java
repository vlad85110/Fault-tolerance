package kirilov.me.crack.manager;

import kirilov.me.crack.dto.ManagerToWorkerRequest;
import kirilov.me.crack.entity.LastRequestId;
import kirilov.me.crack.entity.RequestInfo;
import kirilov.me.crack.entity.RequestStatus;
import kirilov.me.crack.entity.WorkerStatus;
import kirilov.me.crack.repository.LastRequestIdRepository;
import kirilov.me.crack.repository.RequestInfoRepository;
import kirilov.me.crack.repository.WorkerStatusesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@Transactional
public class Manager {
    @Value("${workerCnt}")
    private int workerCnt;
    @Value("${timeout}")
    private long timeout;
//    private UUID lastRequestId;

    private final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private Future<?> timerFuture;
    private final RequestInfoRepository requestInfoRepository;
    private final WorkerStatusesRepository workerStatusesRepository;

    private final LastRequestIdRepository lastRequestIdRepository;
    private final ManagerProducer managerProducer;

    @Autowired
    public Manager(RequestInfoRepository requestInfoRepository,
                   WorkerStatusesRepository workerStatusesRepository, LastRequestIdRepository lastRequestIdRepository, ManagerProducer managerProducer) {
        this.workerStatusesRepository = workerStatusesRepository;
        this.requestInfoRepository = requestInfoRepository;
        this.lastRequestIdRepository = lastRequestIdRepository;
        this.managerProducer = managerProducer;
    }

    public UUID createCrackRequest(String hash, int maxLength) {
        var requestId = UUID.randomUUID();
        RequestInfo info = new RequestInfo(requestId, RequestStatus.IN_PROGRESS, null);
        requestInfoRepository.save(info);

        log.info("created crack task with request id {}", requestId);

        RequestErrorTask errorTask = new RequestErrorTask(requestInfoRepository, requestId);
        timerFuture = timer.schedule(() -> {
            log.info("{} timeout", requestId);
            errorTask.run();
        }, timeout, TimeUnit.MINUTES);

        String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
        var alphabetAsList = Arrays.asList(alphabet.split(""));
        sendRequestToWorkers(requestId, hash, maxLength, alphabetAsList);

        lastRequestIdRepository.save(new LastRequestId("lastRequestId", requestId));
        return requestId;
    }

    public void sendRequestToWorkers(UUID requestId, String hash, int maxLength, List<String> alphabet) {
        var optional = workerStatusesRepository.findById("workerStatuses");
        if (optional.isEmpty()) return;

        var workerStatuses = optional.get();
        var data = workerStatuses.getData();
        data.clear();

        for (int i = 0; i < workerCnt; i++) {
            var request = new ManagerToWorkerRequest(requestId, i, workerCnt, hash, maxLength, alphabet);
            managerProducer.sendMessage(request);
            data.add(WorkerStatus.IN_PROGRESS);
        }

        workerStatusesRepository.save(workerStatuses);
        log.info("sent task with id {} to workers", requestId);
    }

    public void processWorkerAnswer(UUID requestId, int partNumber, List<String> answers) {
        var requestInfoOptional = requestInfoRepository.findById(requestId);
        var workerStatusesOptional = workerStatusesRepository.findById("workerStatuses");

        if (requestInfoOptional.isPresent() && workerStatusesOptional.isPresent()) {
            var requestInfo = requestInfoOptional.get();
            var workerStatuses = workerStatusesOptional.get().getData();

            if (requestInfo.getStatus().equals(RequestStatus.READY)) {
                return;
            }

            log.info("received crack answer from {} worker", partNumber + 1);

            WorkerStatus newWorkerStatus;
            if (!answers.isEmpty()) {
                newWorkerStatus = WorkerStatus.FOUND;

                var password = answers.get(0);
                requestInfo.setData(password);
            } else {
                newWorkerStatus = WorkerStatus.NOT_FOUND;
            }
            workerStatuses.set(partNumber, newWorkerStatus);

            boolean isWorkersReady = true;
            for (var status : workerStatuses) {
                if (status.equals(WorkerStatus.IN_PROGRESS)) {
                    isWorkersReady = false;
                    break;
                }
            }

            if (isWorkersReady) {
                RequestStatus newRequestStatus;
                if (requestInfo.getStatus().equals(RequestStatus.ERROR)) {
                    newRequestStatus = RequestStatus.ERROR;
                } else {
                    newRequestStatus = RequestStatus.READY;
                }

                requestInfo.setStatus(newRequestStatus);

                if (timerFuture != null) {
                    timerFuture.cancel(true);
                }
            }

            requestInfoRepository.save(requestInfo);
            workerStatusesRepository.save(workerStatusesOptional.get());
        }
    }

    public RequestInfo getRequestStatus(UUID requestId) {
        log.info("got request for status with id {}", requestId);

        var optional = requestInfoRepository.findById(requestId);
        if (optional.isPresent()) {
            var info = optional.get();

            if (info.getStatus().equals(RequestStatus.IN_PROGRESS)) {
                return new RequestInfo(requestId, RequestStatus.IN_PROGRESS, null);
            } else {
                return info;
            }
        } else {
            return null;
        }
    }

    public RequestInfo getLastTask() {
        var optional = lastRequestIdRepository.findById("lastRequestId");
        if (optional.isEmpty()) return null;

        var lastRequestId = optional.get().getData();
        return getRequestStatus(lastRequestId);
    }
}
