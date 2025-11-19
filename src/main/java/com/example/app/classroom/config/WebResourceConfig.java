package com.example.app.classroom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String userHome = System.getProperty("user.home");
        Path path = Paths.get(userHome, "files");
        System.out.println(path.toUri().toString()); //  file:///C:/Users/USER/files/

        registry.addResourceHandler("/files/**").addResourceLocations(path.toUri().toString());
    }
}
