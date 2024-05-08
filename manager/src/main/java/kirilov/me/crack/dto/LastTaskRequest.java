package kirilov.me.crack.dto;

import kirilov.me.crack.entity.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LastTaskRequest {
    private UUID lastRequestId;
    private RequestStatus status;
    private String data;
}
