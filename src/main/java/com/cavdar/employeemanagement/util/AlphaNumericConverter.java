package com.cavdar.employeemanagement.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class AlphaNumericConverter {

    public String convert(String input) {
        byte[] utf8Bytes = input.getBytes(StandardCharsets.UTF_8);
        return new String(utf8Bytes, StandardCharsets.UTF_8).replaceAll("[^a-zA-Z0-9]", "");
    }
}
