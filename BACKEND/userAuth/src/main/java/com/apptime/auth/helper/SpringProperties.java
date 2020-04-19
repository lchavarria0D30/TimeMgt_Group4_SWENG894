package com.apptime.auth.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
public class SpringProperties {
    @Value("${predictionEngineHost}")
    private String predictionEngineHost;

    public void setPredictionEngineHost(String predictionEngineHost) {
        this.predictionEngineHost = predictionEngineHost;
    }

    public String getPredictionEngineHost() {
        return predictionEngineHost;
    }
}
