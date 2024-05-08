package kirilov.me.crack.worker;

import kirilov.me.crack.entity.WorkerToManagerRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WorkerProducer {
    private final AmqpTemplate amqpTemplate;

    public WorkerProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMessage(WorkerToManagerRequest request) {
        amqpTemplate.convertAndSend("workerToManager", request);
        log.info("worker send crack result to manager");
    }
}
