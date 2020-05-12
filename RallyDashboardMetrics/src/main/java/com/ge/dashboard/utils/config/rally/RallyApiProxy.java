package com.ge.dashboard.utils.config.rally;

import com.rallydev.rest.RallyRestApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URI;
import java.net.URISyntaxException;

@Profile("proxy")
@Configuration
public class RallyApiProxy extends RallyApiConfig {

    @Value("${rally.url}")
    private String RALLY_URL;
    @Value("${rally.api.key}")
    private String RALLY_API_KEY;
    @Value("${rally.proxy}")
    private String RALLY_PROXY;

    @Bean
    @Override
    public RallyRestApi getRallyConnection() {
        RallyRestApi rallyRestApi = null;
        try {
            rallyRestApi = new RallyRestApi(new URI(RALLY_URL), RALLY_API_KEY);
            rallyRestApi.setProxy(new URI(RALLY_PROXY));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return rallyRestApi;
    }
}
