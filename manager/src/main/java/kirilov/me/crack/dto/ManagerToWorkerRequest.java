package kirilov.me.crack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerToWorkerRequest {
    UUID requestId;
    int partNumber;
    int partCount;
    String hash;
    int maxLength;
    List<String> alphabet;
}
