package xyz.ruhshan.sqsdemo.service;

import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import xyz.ruhshan.sqsdemo.dto.Task;

@Service
@Slf4j
public class TaskProcessorImpl implements TaskProcessor {
    @Override
    @SqsListener(value = "dev-task.std",deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    @MessageMapping
    public void process(@Payload Task task) {
        log.info("Processing task {}", task);
    }
}
