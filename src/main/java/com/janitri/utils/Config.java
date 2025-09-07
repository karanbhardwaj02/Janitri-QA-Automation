package com.janitri.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();
    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load config.properties", e);
        }
    }
    public static String get(String key) {
        String sys = System.getProperty(key);
        return (sys != null && !sys.isEmpty()) ? sys : props.getProperty(key);
    }
}
