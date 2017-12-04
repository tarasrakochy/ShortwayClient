package com.taras.shortway.client.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginPass implements Serializable {

    private int id;

    private String login;
    private String password;
}
