package kirilov.me.crack.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestInfo {
    @Id
    private UUID requestId;
    private RequestStatus status;
    private String data;
}
