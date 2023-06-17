package xyz.ruhshan.sqsdemo.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class MyMessageConverter implements MessageConverter {
    private final ObjectMapper objectMapper;

    public MyMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object fromMessage(Message<?> message, Class<?> targetClass) {
        return null;
    }

    @Override
    public Message<?> toMessage(Object payload, MessageHeaders headers) {
        if (headers != null) {
            MessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(headers, MessageHeaderAccessor.class);
            if (accessor != null && accessor.isMutable()) {
                return MessageBuilder.createMessage(getStringPayload(payload), accessor.getMessageHeaders());
            }
        }

        return MessageBuilder.withPayload(getStringPayload(payload)).copyHeaders(headers).build();
    }

    private String getStringPayload(Object payload){
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
