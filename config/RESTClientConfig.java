package com.se2.alert.config;

import com.se2.alert.properties.AlertProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RESTClientConfig {


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, AlertProperties alertProperties) {

        return builder.setConnectTimeout(alertProperties.restConnectTimeout)
                      .setReadTimeout(alertProperties.restReadTimeout)
                      .build();
    }
}
