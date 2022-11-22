package com.webapp.webapp.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.BeanUtils;
import com.webapp.webapp.Service.*;

import com.webapp.webapp.Exception.MyExceptions;
import com.webapp.webapp.UserD;
import com.webapp.webapp.Model.Input.UserI;
import com.webapp.webapp.Service.UserService;
import com.webapp.webapp.Model.UserDbFeed;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;

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
import java.util.Random;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
//import com.csye6225.application.objects.*;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.SnsClientBuilder;
import software.amazon.awssdk.services.sns.model.SnsResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import com.webapp.webapp.Model.*;
import com.webapp.webapp.Repository.*;


@RestController
public class MyController {

    Logger logger = LoggerFactory.getLogger(HealthController.class);
    StatsDClient statsd = new NonBlockingStatsDClientBuilder()
            .prefix("statsd")
            .hostname("localhost")
            .port(8125)
            .build();

    private UserService userService;

    public MyController(UserService userService) {
        super();
        this.userService = userService;
    }

    @Autowired
    PublisherServiceImpl messagePublisher;
    private DynamoDB dynamoDB;

    private static String tableName = "emailToken";
    private AmazonDynamoDB client;


    @PostConstruct
    void init(){
        client =  AmazonDynamoDBClientBuilder.standard().withCredentials(new InstanceProfileCredentialsProvider(false))
                .withRegion("us-east-1").build();

        dynamoDB = new DynamoDB(client);
    }


public boolean validUser(HttpServletRequest request){

    String payload = request.getHeader("Authorization");
    if(payload != null) {
        String hash = payload.split(" ")[1];
        String usr_pass = new String(Base64.getDecoder().decode(hash));
        String user = usr_pass.split(":")[0];
        String pass = usr_pass.split(":")[1];
        if(user.equalsIgnoreCase("admin") && pass.equals("123")) {
            return true;
        }
        else return false;
    }
    return true;
    }


    // Add users
    @PostMapping(path = "/v1/account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody UserDbFeed userDetails, HttpServletRequest req) {

        long startTime = System.currentTimeMillis();
        statsd.count("AddUsr", 1, null);
        logger.info("Add User endpoint hit");

        boolean isAuthenticated = validUser(req);
        Table table = dynamoDB.getTable(tableName);
        Random rand = new Random();
        String randomNo = Integer.toString(rand.nextInt());

        Item item = new Item().withString("emailid", userDetails.getEmail())
                        .withLong("ttl",(System.currentTimeMillis() / 1000L)+ 60)
                        .withString("token",randomNo );
                table.putItem(item);

                Message message = new Message(userDetails.getFirstName(),randomNo,"publishing message in progress!");
                logger.info(message.toString());

                messagePublisher.publish(message);
                logger.info("Message published!");

        if(isAuthenticated){
        try {
            if (userDetails.getEmail().isEmpty() || userDetails.getFirstName().isEmpty()
                    || userDetails.getLastName().isEmpty() || userDetails.getPassword().isEmpty()) {
                throw new MyExceptions.ControllerException("400", "Please fill in all the details.");
            }
            UserI userRest = new UserI();
            UserD userDTO = new UserD();
            BeanUtils.copyProperties(userDetails, userDTO);

            userRest = userService.createUser(userDTO);

            statsd.recordExecutionTime("AddUsr", System.currentTimeMillis() - startTime, null);
            return new ResponseEntity<>(userRest, HttpStatus.CREATED);

        } catch (MyExceptions be) {
            MyExceptions.ControllerException ce = new MyExceptions.ControllerException(be.getErrorCode(), be.getErrorMessage());
            return new ResponseEntity<>(ce, HttpStatus.BAD_REQUEST);
        } catch (MyExceptions.ControllerException ee) {
            return new ResponseEntity<>(ee, HttpStatus.BAD_REQUEST);
        }
        }else {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
        }
    }

    // @GetMapping(value = "/verifyUserEmail",
    //         produces = {MediaType.APPLICATION_JSON_VALUE})
    // public ResponseEntity<?> verifyUser(@RequestParam("email") String email, @RequestParam("token") String token ){
    
    //     logger.info("verifyUser requested");

    //     Table table = dynamoDB.getTable(tableName);
    //     Map<String,Object> itemmap = table.getItem("emailid", email, "emailid email token",null).asMap();
    //     if(itemmap.get("emailid").equals(email) && itemmap.get("token").equals(token)){
    //         User presentUser = UserRepository.findByUsername(email);
    //         presentUser.setVerified(true);
    //         userRepository.save(presentUser);
    //     }

    //     return ResponseEntity.status(HttpStatus.ACCEPTED).body( null);
    // }

    // Dispaly/Get Users
    @GetMapping(path = "/v1/account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable Long id, HttpServletRequest req) {

        long startTime = System.currentTimeMillis();
        statsd.count("GetUsr", 1, null);
        logger.info("Get User endpoint hit");

        boolean isAuthenticated = validUser(req);
        if(isAuthenticated){
        try {
            if (id == null) {
                throw new MyExceptions.ControllerException("403", "Bad url");
            }
            UserI userRest = new UserI();
            userRest = userService.findUser(id);
            statsd.recordExecutionTime("GetUsr", System.currentTimeMillis() - startTime, null);
            return new ResponseEntity<>(userRest, HttpStatus.OK);
        } catch (MyExceptions.ControllerException ee) {
            return new ResponseEntity<>(ee, HttpStatus.FORBIDDEN);
        } catch (MyExceptions be) {
            MyExceptions.ControllerException ce = new MyExceptions.ControllerException(be.getErrorCode(), be.getErrorMessage());
            return new ResponseEntity<>(ce, HttpStatus.FORBIDDEN);
        }
    } else {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
        }}

    // Update/Put Users
    @PutMapping(path = "/v1/account/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> UpdateUser(@RequestBody UserDbFeed userDetails, @PathVariable long id, HttpServletRequest req) {

        long startTime = System.currentTimeMillis();
        statsd.count("PutUsr", 1, null);
        logger.info("Put User endpoint hit");

        boolean isAuthenticated = validUser(req);
        if(isAuthenticated){
        try {
            if (userDetails.getEmail().isEmpty() || userDetails.getFirstName().isEmpty()
                    || userDetails.getLastName().isEmpty() || userDetails.getPassword().isEmpty()) {
                throw new MyExceptions.ControllerException("400", "Please fill in all the details.");
            }

            UserD userDTO = new UserD();
            UserI userRest = new UserI();
            BeanUtils.copyProperties(userDetails, userDTO);
            userRest = userService.findUserByMailId(userDTO);
            if (id == userRest.getId()) {
                UserD userDTO1 = new UserD();
                UserI userRest1 = new UserI();
                BeanUtils.copyProperties(userDetails, userDTO1);
                userRest = userService.updateUser(userDTO1);
                statsd.recordExecutionTime("PutUsr", System.currentTimeMillis() - startTime, null);
                return new ResponseEntity<>("Update successful",HttpStatus.ACCEPTED);
            } else {
                throw new MyExceptions.ControllerException("403", "Access Denied");
            }

        } catch (MyExceptions.ControllerException ee) {
            if (ee.getErrorCode().equals("403")) {
                return new ResponseEntity<>(ee, HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(ee, HttpStatus.BAD_REQUEST);
            }

        } catch (MyExceptions be) {
            MyExceptions.ControllerException ce = new MyExceptions.ControllerException(be.getErrorCode(), be.getErrorMessage());
            return new ResponseEntity<>(ce, HttpStatus.NOT_FOUND);
        }}
        else {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
        }

    }

}
