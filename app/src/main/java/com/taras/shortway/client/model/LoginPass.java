package com.taras.shortway.client.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginPass {

    private int id;

    private String login;
    private String password;
}
