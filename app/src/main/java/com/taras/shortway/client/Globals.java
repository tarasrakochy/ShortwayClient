package com.taras.shortway.client;

import com.taras.shortway.client.model.User;

import lombok.Getter;
import lombok.Setter;

public class Globals {
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Globals.user = user;
    }
}
