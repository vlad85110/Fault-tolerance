package kirilov.me.crack.worker;

import com.rabbitmq.client.Channel;
import kirilov.me.crack.dto.ManagerToWorkerRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WorkerConsumer {
    private final Worker worker;

    public WorkerConsumer(Worker worker) {
        this.worker = worker;
    }

    @RabbitListener(queues = "managerToWorker")
    public void handleMessage(ManagerToWorkerRequest message, Channel channel,
                              @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        worker.crackHash(message);

        channel.basicAck(tag, false);
    }
}
