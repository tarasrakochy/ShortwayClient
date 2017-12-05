package com.taras.shortway.client.model;

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
    
    private Date date;

    private Date time;

    private User driver;

    private List<String> transitionals;
}
