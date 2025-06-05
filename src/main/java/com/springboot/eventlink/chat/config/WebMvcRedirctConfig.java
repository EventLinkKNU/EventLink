package com.springboot.eventlink.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcRedirectConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        // SPA 대응: .(확장자)가 없는 모든 경로는 React Router로 forward
        registry.addViewController("/{path:[^\\.]*}")
                .setViewName("forward:/");

        registry.addViewController("/")
                .setViewName("forward:/");
    }
}
