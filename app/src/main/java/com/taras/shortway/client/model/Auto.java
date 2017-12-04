package com.taras.shortway.client.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Auto implements Serializable {

    private int id;

    private String country;

    private String number;

    private String brand;

    private String type;

    private String model;

    private String color;

    private byte[] photo;
}
