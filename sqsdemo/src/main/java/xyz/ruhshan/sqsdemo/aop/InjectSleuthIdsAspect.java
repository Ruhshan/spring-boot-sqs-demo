package xyz.ruhshan.sqsdemo.aop;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import io.awspring.cloud.messaging.core.SqsMessageHeaders;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class InjectSleuthIdsAspect {
    private static class SleuthIds {
        Long traceId;
        Long spanId;
    }

    private final Tracer tracer;

    public InjectSleuthIdsAspect(Tracer tracer) {
        this.tracer = tracer;
    }

    @Around("@annotation(xyz.ruhshan.sqsdemo.aop.InjectSleuthIds)")
    public Object injectSleuthIdsAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {

        SleuthIds sleuthIds = getSleuthIdsFromPjp(proceedingJoinPoint);

        TraceContext traceContext = TraceContext.newBuilder()
                .traceId(sleuthIds.traceId)
                .spanId(sleuthIds.spanId)
                .build();

        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(traceContext));

        try (var scope = tracer.withSpanInScope(span)) {
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.error("Caught exception during proceeding joint point {}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            span.finish();
        }
    }

    private SleuthIds getSleuthIdsFromPjp(ProceedingJoinPoint proceedingJoinPoint) {

        var args = proceedingJoinPoint.getArgs();

        SqsMessageHeaders sqsMessageHeaders = null;

        for (Object arg : args) {
            if (arg instanceof SqsMessageHeaders) {
                sqsMessageHeaders = (SqsMessageHeaders) arg;
            }
        }

        if (sqsMessageHeaders == null) {
            throw new RuntimeException("No SqsHeaders found with " + proceedingJoinPoint.getTarget().toString());
        }

        xyz.ruhshan.sqsdemo.aop.InjectSleuthIdsAspect.SleuthIds sleuthIds = new xyz.ruhshan.sqsdemo.aop.InjectSleuthIdsAspect.SleuthIds();
        sleuthIds.traceId = sqsMessageHeaders.get("SleuthTraceId", Long.class);
        sleuthIds.spanId = sqsMessageHeaders.get("SleuthSpanId", Long.class);
        return sleuthIds;
    }


}
