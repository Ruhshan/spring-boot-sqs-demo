package xyz.ruhshan.sqsdemo.service;

import brave.Tracer;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.ruhshan.sqsdemo.dto.Task;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class QueueServiceImpl implements QueueService {
    private final QueueMessagingTemplate queueMessagingTemplate;
    private final String taskQueue;
    private final Tracer tracer;

    public QueueServiceImpl(QueueMessagingTemplate queueMessagingTemplate,
                            @Value("${queue.task}") String taskQueue, Tracer tracer) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.taskQueue = taskQueue;
        this.tracer = tracer;
    }

    @Override
    public void publishTask(Task task) {

        task.setId(UUID.randomUUID().toString());
        task.setArrivedAt(Instant.now());
        log.info("Publishing task to queue {}", task);

        queueMessagingTemplate.convertAndSend(taskQueue, task, headers());

    }

    private Map<String, Object> headers() {
        return Map.of(
                "SleuthTraceId",tracer.currentSpan().context().traceId(),
                "SleuthSpanId", tracer.currentSpan().context().spanId());
    }
}
