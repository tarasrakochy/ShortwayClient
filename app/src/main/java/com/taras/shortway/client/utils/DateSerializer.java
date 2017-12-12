package com.taras.shortway.client.utils;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

public class DateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.getTime());
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Date(json.getAsLong());
    }
}
