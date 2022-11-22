package com.webapp.webapp.Service;

import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestorService {

    public static PublishRequest build(String topicArn, Message message) {
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        attributes.put("email", buildAttribute(message.getEmail(), "String"));
        attributes.put("token", buildAttribute(message.getToken(), "String"));
        attributes.put("messagetype", buildAttribute(message.getMessagetype(), "String"));

        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message("Hi, This is webapp speaking")
                .messageAttributes(attributes)
                .build();

        return request;
    }

    private static MessageAttributeValue buildAttribute(String value, String dataType) {
        return MessageAttributeValue.builder()
                .dataType(dataType)
                .stringValue(value)
                .build();
    }
}