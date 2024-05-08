package kirilov.me.crack.manager;

import kirilov.me.crack.dto.LastTaskRequest;
import kirilov.me.crack.dto.ManagerRequest;
import kirilov.me.crack.dto.ManagerToUserResponse;
import kirilov.me.crack.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ManagerController {
    private final Manager manager;

    @Autowired
    public ManagerController(Manager manager) {
        this.manager = manager;
    }

    @PostMapping("/api/hash/crack")
    public ManagerToUserResponse hashCrack(@RequestBody ManagerRequest request) {
        var hash = request.getHash();
        var maxLength = request.getMaxLength();

        var requestId = manager.createCrackRequest(hash, maxLength);
        return new ManagerToUserResponse(requestId);
    }

    @GetMapping("/api/hash/status")
    public RequestInfo crackStatus(@RequestParam UUID requestId) {
        return manager.getRequestStatus(requestId);
    }

    @GetMapping("/lastTask")
    public RequestInfo getLastTask() {
        return manager.getLastTask();
    }
}
