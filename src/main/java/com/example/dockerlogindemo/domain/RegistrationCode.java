package com.example.dockerlogindemo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
public class RegistrationCode {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String username;

    @Column
    private String token;

    @Column
    private Date dateCreated;

    public RegistrationCode() {
    }

    public RegistrationCode(String username) {
        this.username = username;
        this.token = UUID.randomUUID().toString().replaceAll("-", "");
        this.dateCreated = Calendar.getInstance().getTime();
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
