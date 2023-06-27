package xyz.ruhshan.sqsdemo.service;

import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskProcessorImpl implements TaskProcessor {
    @Override
    @SqsListener(value = "dev-task.std",deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    @MessageMapping
    public void process(String task) {
        log.info("Processing task {}", task);
    }
}
