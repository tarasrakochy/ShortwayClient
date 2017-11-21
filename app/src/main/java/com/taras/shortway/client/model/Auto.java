package com.taras.shortway.client.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Auto {

    private int id;

    private String country;

    private String number;

    private String brand;

    private String type;

    private String model;

    private String color;

    private byte[] photo;
}
