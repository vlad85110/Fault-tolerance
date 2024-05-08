package kirilov.me.crack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerToWorkerRequest {
    private UUID requestId;
    private int partNumber;
    private int partCount;
    private String hash;
    private int maxLength;
    private List<String> alphabet;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ManagerToWorkerRequest that = (ManagerToWorkerRequest) obj;
        return partNumber == that.partNumber &&
                requestId.equals(that.requestId);
    }
}
