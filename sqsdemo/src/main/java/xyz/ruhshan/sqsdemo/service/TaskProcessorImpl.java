package xyz.ruhshan.sqsdemo.service;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import xyz.ruhshan.sqsdemo.dto.Task;

import java.util.Map;

@Service
@Slf4j
public class TaskProcessorImpl implements TaskProcessor {
    private final Tracer tracer;

    public TaskProcessorImpl(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    @SqsListener(value = "dev-task.std",deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    @MessageMapping
    public void process(@Payload Task task, @Headers Map<String, Object> headers) {

        var traceId = headers.get("SleuthTraceId");
        var spanId = headers.get("SleuthSpanId");

        TraceContext traceContext = TraceContext.newBuilder()
                .traceId((Long) traceId)
                .spanId((Long) spanId)
                .build();

        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(traceContext)).start();

        try(Tracer.SpanInScope scope = tracer.withSpanInScope(span.start())){
            log.info("Processing task {}", task);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            span.finish();
        }

    }

}
