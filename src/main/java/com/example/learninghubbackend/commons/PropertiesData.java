package com.example.learninghubbackend.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertiesData {
    private final Environment environment;
    private String client;
    private Integer rateLimit;
    private Integer maxRateLimit;

    public String getClient() {
        if (client == null) {
            client = environment.getProperty("app.client.url");
        }

        return client;
    }

    public Integer getRateLimit() {
        if (rateLimit == null) {
            rateLimit = environment.getProperty("app.api.rateLimit", Integer.class);
        }

        return rateLimit;
    }

    public Integer getMaxRateLimit() {
        if (maxRateLimit == null) {
            maxRateLimit = environment.getProperty("app.api.maxQueue", Integer.class);
        }

        return maxRateLimit;
    }
}
