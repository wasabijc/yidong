package com.example.utils;

import java.util.UUID;

public class UuidUtils {
    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
