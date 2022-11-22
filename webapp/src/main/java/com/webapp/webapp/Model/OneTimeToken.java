package com.webapp.webapp.Model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName = "emailToken")
public class OneTimeToken {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String token;

    private String emailid;

}