package com.uade.tpo.demo.controllers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    private final String uploadDir;

    public ResourceConfig(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expose files under /uploads/** mapped to the filesystem upload directory
        String location = "file:" + uploadDir + "/";
        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
    }
}
