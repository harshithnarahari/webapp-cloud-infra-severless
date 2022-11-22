package com.webapp.webapp.Service;

import com.webapp.webapp.Service.Message;
import com.webapp.webapp.Service.RequestorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.services.sns.model.SnsResponse;

import javax.annotation.PostConstruct;

@Service
public class PublisherServiceImpl implements PublisherService {
    Logger logger = LoggerFactory.getLogger(PublisherServiceImpl.class);

    private SnsClient snsClient;
    private String topicArn ="arn:aws:sns:us-east-1:474665934708:web-app";
    @PostConstruct
    public void init() {
        this.snsClient = SnsClient.builder().region(Region.US_EAST_1).credentialsProvider(software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider.builder().build()).build();
    }

    @Override
    public void publish(Message message) {

        try {
            PublishRequest request = RequestorService.build(topicArn, message);
            logger.info("Request: {}", request);

            PublishResponse publishResponse = snsClient.publish(request);
            logger.info("Publish response: {}", publishResponse);

        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}