package xyz.ruhshan.sqsdemo.init;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class CreateSqsQueue implements CommandLineRunner {
    private final String profile;
    private final AmazonSQSAsync amazonSQSAsync;
    private final String taskQueue;

    public CreateSqsQueue(@Value("${spring.profiles.active}") String profile,
                          @Value("${queue.task}") String taskQueue,
                          AmazonSQSAsync amazonSQSAsync
                          ) {
        this.profile = profile;
        this.amazonSQSAsync = amazonSQSAsync;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run(String... args) throws Exception {
        if(Objects.equals(profile, "dev")){
            createQueues(taskQueue);
        }
    }

    private void createQueues(String queueName){

        log.info("Creating queue {}", queueName);

        CreateQueueRequest createQueueRequest = new CreateQueueRequest();
        createQueueRequest.setQueueName(queueName);
        createQueueRequest.setAttributes(
                Map.of(QueueAttributeName.ContentBasedDeduplication.name(),"true"));

        amazonSQSAsync.createQueueAsync(createQueueRequest);

    }
}
