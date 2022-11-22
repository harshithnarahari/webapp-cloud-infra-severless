package com.webapp.webapp.Model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "file_data")

public class Docu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "date_created", nullable = false)
    private Date date_created = new Date(System.currentTimeMillis());
    // @Column(name = "s3_bucket_path", nullable = false, unique = true)
    // private String s3_bucket_path;

}
