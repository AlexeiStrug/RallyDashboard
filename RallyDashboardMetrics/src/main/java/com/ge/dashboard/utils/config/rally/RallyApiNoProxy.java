package com.ge.dashboard.utils.config.rally;

import com.rallydev.rest.RallyRestApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile("default")
public class RallyApiNoProxy extends RallyApiConfig {

    @Value("${rally.url}")
    private String RALLY_URL;
    @Value("${rally.api.key}")
    private String RALLY_API_KEY;

    @Bean
    @Override
    public RallyRestApi getRallyConnection() {
        RallyRestApi restApi = null;
        try {
            restApi = new RallyRestApi(new URI(RALLY_URL), RALLY_API_KEY);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return restApi;
    }
}
