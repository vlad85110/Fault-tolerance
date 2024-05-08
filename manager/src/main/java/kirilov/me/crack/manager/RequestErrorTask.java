package kirilov.me.crack.manager;

import kirilov.me.crack.entity.RequestStatus;
import kirilov.me.crack.repository.RequestInfoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class RequestErrorTask implements Runnable {
    private final RequestInfoRepository requestInfoRepository;
    private final UUID requestId;

    public RequestErrorTask(RequestInfoRepository requestInfoRepository, UUID requestId) {
        this.requestInfoRepository = requestInfoRepository;
        this.requestId = requestId;
    }

    @Override
    @Transactional
    public void run() {
        var optional = requestInfoRepository.findById(requestId);
        if (optional.isPresent()) {
            var requestInfo = optional.get();
            requestInfo.setStatus(RequestStatus.ERROR);
            requestInfoRepository.save(requestInfo);
        }
    }
}
