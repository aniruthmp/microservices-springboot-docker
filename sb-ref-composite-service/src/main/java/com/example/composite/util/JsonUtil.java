package com.example.composite.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by a.c.parthasarathy on 10/26/16.
 */
public class JsonUtil {

    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(obj);
    }

}
