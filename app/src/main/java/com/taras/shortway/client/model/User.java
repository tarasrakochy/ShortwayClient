package com.taras.shortway.client.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private int id;

    private byte[] avatar;

    private String phone;

    private String email;

    private LoginPass loginPass;

    private Auto auto;

    private UserInfo userInfo;
}
