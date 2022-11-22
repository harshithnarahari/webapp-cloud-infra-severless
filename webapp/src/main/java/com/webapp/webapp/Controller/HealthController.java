package com.webapp.webapp.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import javax.annotation.PostConstruct;

@RestController
public class HealthController {

    Logger logger = LoggerFactory.getLogger(HealthController.class);
    StatsDClient statsd = new NonBlockingStatsDClientBuilder()
            .prefix("statsd")
            .hostname("localhost")
            .port(8125)
            .build();


    @GetMapping("/healthz")
    public ResponseEntity<?> healthz() {
        long startTime = System.currentTimeMillis();
        statsd.count("Health", 1, null);
        logger.info("healthz endpoint hit");

        statsd.recordExecutionTime("Health", System.currentTimeMillis() - startTime, null);
        return new ResponseEntity<>("Hi, Welcome :)",HttpStatus.OK);
    }
}
