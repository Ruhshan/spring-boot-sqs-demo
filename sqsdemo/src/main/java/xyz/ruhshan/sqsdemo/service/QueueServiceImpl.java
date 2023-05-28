package xyz.ruhshan.sqsdemo.service;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.ruhshan.sqsdemo.dto.Task;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class QueueServiceImpl implements QueueService {
    private final QueueMessagingTemplate queueMessagingTemplate;
    private final String taskQueue;

    public QueueServiceImpl(QueueMessagingTemplate queueMessagingTemplate,
                            @Value("${queue.task}") String taskQueue) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.taskQueue = taskQueue;
    }

    @Override
    public void publishTask(Task task) {

        task.setId(UUID.randomUUID().toString());
        log.info("Publishing task to queue {}", task);

        queueMessagingTemplate.convertAndSend(taskQueue, task);

    }
}
