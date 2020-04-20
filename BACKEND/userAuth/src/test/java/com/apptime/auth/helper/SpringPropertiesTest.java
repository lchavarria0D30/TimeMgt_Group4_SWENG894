package com.apptime.auth.helper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@SpringBootTest
public class SpringPropertiesTest {
    @Autowired
    private SpringProperties properties;

    @Test
    public void test() {
        assertNotNull(properties.getPredictionEngineHost());

        properties.setPredictionEngineHost(null);
        assertNull(properties.getPredictionEngineHost());
    }
}
