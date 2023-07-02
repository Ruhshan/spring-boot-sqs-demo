package xyz.ruhshan.sqsdemo.service;

import brave.Tracer;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import xyz.ruhshan.sqsdemo.aop.InjectSleuthIds;
import xyz.ruhshan.sqsdemo.dto.Task;

import java.util.Map;

@Service
@Slf4j
public class TaskProcessorImpl implements TaskProcessor {

    @Override
    @SqsListener(value = "dev-task.std",deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    @MessageMapping
    @InjectSleuthIds
    public void process(@Payload Task task, @Headers Map<String, Object> headers) {

        log.info("Processing task {}", task);

    }

}
