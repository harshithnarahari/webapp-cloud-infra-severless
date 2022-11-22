package com.webapp.webapp.Model;

import lombok.Data;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Entity
@Table(name = "users")
public class User {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "firstname", nullable = false)
    private String firstName;
    @Column(name = "lastname", nullable = false)
    private String lastName;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String encryptedPassword;;
    @Column(name = "account_created", nullable = false, updatable = false)
    @CreatedDate
    private Date account_created = new Date(System.currentTimeMillis());
    @Column(name = "account_updated", nullable = false)
    @LastModifiedDate
    private Date account_updated = new Date(System.currentTimeMillis());

}
