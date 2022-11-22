package com.webapp.webapp.Model.Input;

import lombok.Data;

import java.util.Date;

@Data
public class UserI {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Date account_created;
    private Date account_updated;
}
