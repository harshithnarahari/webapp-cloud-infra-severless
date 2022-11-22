package com.webapp.webapp.Service;


import com.webapp.webapp.Service.Message;
import software.amazon.awssdk.services.sns.model.SnsResponse;

public interface PublisherService {
    void publish(Message message);
}