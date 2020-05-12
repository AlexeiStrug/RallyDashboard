package com.ge.dashboard.utils.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
//        registry.addMapping("/**")
//                .allowedMethods("GET, OPTIONS, HEAD")
//                .allowedOrigins("*")
//                .allowedHeaders("origin, content-type, accept")
//                .maxAge(1209600);
    }
}
