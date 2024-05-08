package kirilov.me.crack.dto;

import java.util.UUID;

public class ManagerToUserResponse {
    private UUID requestId;

    public ManagerToUserResponse(UUID requestId) {
        this.requestId = requestId;
    }

    public ManagerToUserResponse() {
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }
}
