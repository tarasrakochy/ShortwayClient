package com.taras.shortway.client.model;

import com.taras.shortway.client.model.enums.Gender;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfo {

    private int id;

    private Gender gender;

    private String information;

    private int year;
}
