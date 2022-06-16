package com.javamentor.qa.platform.webapp.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/login");
        registry.addRedirectViewController("/logout","/login");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/question/add").setViewName("question");
        registry.addViewController("/users").setViewName("user");
        registry.addViewController("/tags").setViewName("tags");
        registry.addViewController("/question/{id}").setViewName("questionById");
        registry.addViewController("/user/pagination").setViewName("index");
        registry.addViewController("/questions").setViewName("questions");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/js/**", "/static/css/**" )
                .addResourceLocations("classpath:/static/js/")
                .addResourceLocations("classpath:/static/css/");

    }
}
