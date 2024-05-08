package kirilov.me.crack.dto;

import org.springframework.lang.NonNull;

public class ManagerRequest {
    @NonNull
    private String hash;
    private int maxLength;

    public ManagerRequest(@NonNull String hash, int maxLength) {
        this.hash = hash;
        this.maxLength = maxLength;
    }

    @NonNull
    public String getHash() {
        return hash;
    }

    public void setHash(@NonNull String hash) {
        this.hash = hash;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
