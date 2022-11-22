package com.webapp.webapp.Model.Response;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorMessage {

    private int errorId;
    private String errorMessage;
    private Date timeStamp;
}
