package com.lcp.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@ConfigurationProperties(prefix = "spring.datasource.druid-app")
public class AppDbDruidConfig extends HashMap<String, String> {
    public HashMap<String, String> getDruidValue() {
        HashMap<String, String> map = new HashMap<>();
        for (String key : this.keySet()) {
            map.put("druid." + key, get(key));
        }
        return map;
    }
}
