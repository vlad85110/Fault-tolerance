package kirilov.me.crack.worker;

import jakarta.xml.bind.DatatypeConverter;
import kirilov.me.crack.dto.ManagerToWorkerRequest;
import kirilov.me.crack.entity.WorkerToManagerRequest;
import lombok.extern.slf4j.Slf4j;
import org.paukov.combinatorics3.Generator;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
@Component
public class Worker {

    private final WorkerProducer workerProducer;

    private final List<ManagerToWorkerRequest> requests;
    private final List<WorkerToManagerRequest> responses;

    public Worker(WorkerProducer workerProducer, List<ManagerToWorkerRequest> requests, List<WorkerToManagerRequest> responses) {
        this.workerProducer = workerProducer;
        this.requests = requests;
        this.responses = responses;
    }

    public void crackHash(ManagerToWorkerRequest request) {
        boolean containsRequest = false;

        for (var req : requests) {
            if (request.equals(req)) {
                containsRequest = true;

                for (var resp : responses) {
                    if (resp.getRequestId().equals(req.getRequestId())
                            && resp.getPartNumber() == req.getPartNumber()) {
                        workerProducer.sendMessage(resp);
                        log.info("resend result for request id {}", request.getRequestId());
                        return;
                    }
                }

                break;
            }
        }

        var requestId = request.getRequestId();
        var partNumber = request.getPartNumber();
        var partCount = request.getPartCount();
        var hash = request.getHash();
        var maxLength = request.getMaxLength();
        var alphabet = request.getAlphabet();

        log.info("received crack request");

        var result = bruteForce(partNumber, partCount, hash, maxLength, alphabet);

        List<String> resultList;
        if (result.isPresent()) {
            log.info("found password - {}", result.get());
            resultList = List.of(result.get());
        } else {
            log.info("not found password");
            resultList = Collections.emptyList();
        }

        var response = new WorkerToManagerRequest(requestId, partNumber, resultList);
        responses.add(response);

        if (!containsRequest) {
            log.info("adding request");
            requests.add(request);
        }

        workerProducer.sendMessage(response);
    }

    public Optional<String> bruteForce(int partNumber, int partCount, String hash, int maxLength, List<String> alphabet) {
        for (int i = 1; i <= maxLength; i++) {
            log.info("start length {}", i);

            var iterator = Generator.permutation(alphabet).withRepetitions(i).iterator();

            var combinationsCnt = (long) Math.pow(alphabet.size(), i);

            var startIndex = getStartIndex(combinationsCnt, partNumber, partCount);
            var endIndex = getEndIndex(combinationsCnt, partNumber, partCount);

            var j = 0;

            while (iterator.hasNext()) {
                var combinationAsList = iterator.next();

                if (j >= startIndex && j <= endIndex) {
                    var combination = String.join("", combinationAsList);
                    var combinationHash = md5Hash(combination);

                    if (hash.equals(combinationHash)) {
                        return Optional.of(combination);
                    }

                }

                if (j > endIndex) {
                    break;
                }

                j++;
            }

            log.info("end length {}", i);
        }

        return Optional.empty();
    }

    public static long getStartIndex(long size, long partNumber, long partCount) {
        return partNumber * size / partCount;
    }

    public static long getEndIndex(long size, long partNumber, long partCount) {
        return Math.min(size - 1, ((partNumber + 1) * size / partCount) - 1);
    }

    public static String md5Hash(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        md.update(password.getBytes());
        byte[] digest = md.digest();

        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }
}
