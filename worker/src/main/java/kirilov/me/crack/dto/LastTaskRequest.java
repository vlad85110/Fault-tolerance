package kirilov.me.crack.dto;

import kirilov.me.crack.entity.RequestStatus;

import java.util.UUID;

public class LastTaskRequest {
    private UUID lastRequestId;
    private RequestStatus status;
    private String data;

    public LastTaskRequest() {
    }

    public LastTaskRequest(UUID lastRequestId, RequestStatus status, String data) {
        this.lastRequestId = lastRequestId;
        this.status = status;
        this.data = data;
    }

    public UUID getLastRequestId() {
        return lastRequestId;
    }

    public void setLastRequestId(UUID lastRequestId) {
        this.lastRequestId = lastRequestId;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
