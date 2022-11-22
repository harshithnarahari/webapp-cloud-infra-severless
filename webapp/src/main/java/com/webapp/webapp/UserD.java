package com.webapp.webapp;

import lombok.Data;

@Data
public class UserD {

    private static final long serialVersionUID = 1L;
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
