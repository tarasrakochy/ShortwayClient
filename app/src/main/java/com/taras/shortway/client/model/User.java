package com.taras.shortway.client.model;

import com.google.gson.annotations.JsonAdapter;
import com.taras.shortway.client.utils.ByteArrayDeserializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {

    private int id;

    @JsonAdapter(ByteArrayDeserializer.class)
    private byte[] avatar;

    private String name;

    private String surname;

    private String phone;

    private String email;

    private LoginPass loginPass;

    private UserInfo userInfo;
}
