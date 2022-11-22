package com.webapp.webapp.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Message {
    private String email;
    private String token;
    private String messagetype;
}