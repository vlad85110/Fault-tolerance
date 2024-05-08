package kirilov.me.crack.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkerToManagerRequest {
    private UUID requestId;
    private int partNumber;
    private List<String> words;
}
