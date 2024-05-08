package kirilov.me.crack.manager;

import com.rabbitmq.client.Channel;
import kirilov.me.crack.entity.WorkerToManagerRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ManagerConsumer {
    private final Manager manager;

    public ManagerConsumer(Manager manager) {
        this.manager = manager;
    }

    @RabbitListener(queues = "workerToManager")
    public void handleMessage(WorkerToManagerRequest message, Channel channel,
                              @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        manager.processWorkerAnswer(message.requestId(), message.partNumber(), message.words());
        channel.basicAck(tag, false);
    }
}
