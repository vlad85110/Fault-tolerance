package kirilov.me.crack.manager;

import kirilov.me.crack.dto.ManagerToWorkerRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ManagerProducer {
    private final AmqpTemplate amqpTemplate;

    public ManagerProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMessage(ManagerToWorkerRequest message) {
        amqpTemplate.convertAndSend("managerToWorker", message);
    }
}
