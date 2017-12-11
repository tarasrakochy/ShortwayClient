package com.taras.shortway.client.model;

import com.google.gson.annotations.JsonAdapter;
import com.taras.shortway.client.utils.DateSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Trip implements Serializable {

    private int id;

    private String fromPoint;

    private String toPoint;

    @JsonAdapter(value = DateSerializer.class)
    private Date date;

    private int price;

    private int passengersMaxCount;

    private List<String> transitionals;
}
