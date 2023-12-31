package xyz.ruhshan.sqsdemo.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.config.SimpleMessageListenerContainerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.HeaderMethodArgumentResolver;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import software.amazon.awssdk.regions.Region;

import java.util.List;

@Configuration
public class SqsConfig {
    private final int BATCH_SIZE = 10;
    private final String queueEndpoint;

    public SqsConfig(@Value("${queue.endpoint}") String queueEndpoint) {
        this.queueEndpoint = queueEndpoint;
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync(@Value("${spring.profiles.active}") String profile){
        if(profile.equals("dev")){
            return AmazonSQSAsyncClientBuilder
                    .standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(queueEndpoint,
                            Region.AP_SOUTHEAST_1.id()))
                    .build();
        }
        else{
            return AmazonSQSAsyncClientBuilder
                    .standard()
                    .withRegion(Region.AP_SOUTHEAST_1.id())
                    .build();
        }
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync, MyMessageConverter myMessageConverter) {
        QueueMessagingTemplate queueMessagingTemplate = new QueueMessagingTemplate(amazonSQSAsync);
        queueMessagingTemplate.setMessageConverter(myMessageConverter);
        return queueMessagingTemplate;

    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(ObjectMapper objectMapper, AmazonSQSAsync amazonSQSAsync) {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setStrictContentTypeMatch(false);

        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
        factory.setAmazonSqs(amazonSQSAsync);

        List<HandlerMethodArgumentResolver> resolvers = List.of(
                new PayloadMethodArgumentResolver(messageConverter,null, false));
        factory.setArgumentResolvers(resolvers);

        return factory;
    }

}
