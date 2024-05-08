package kirilov.me.crack.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Document(collection = "manager_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManagerData {
    @Id
    String id;
    private Map<UUID, RequestInfo> requestInfoMap;
    private List<WorkerStatus> workerStatuses;
    private UUID lastRequestId;
}
