package com.carnival.auth.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by a.c.parthasarathy on 11/3/16.
 */
@Entity
@Data
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    private String username, password;
    private boolean active;

    public Account(String username, String password, boolean active) {
        this.username = username;
        this.password = password;
        this.active = active;
    }

    public Account(){}

}
