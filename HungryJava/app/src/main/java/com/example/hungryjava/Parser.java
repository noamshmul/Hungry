package com.example.hungryjava;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    static final String CODE = "code";
    static final String ID = "inventory_id";
    static final String PASS = "password";


    static HashMap<String, Object> parse(int code, String inv_id, String password, HashMap<String, Object> params)
    {
        HashMap<String, Object> request = new HashMap<>();
        request.put(CODE, code);
        request.put(ID, inv_id);
        request.put(PASS, password);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            request.put(key, value);
        }
        return request;
    }
}
