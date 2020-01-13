package com.github.dapeng.dms.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;

//@Configuration
//@EnableWebMvc
public class StaticResourceHandler implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // src/main/resources/...
        registry
                .addResourceHandler("/**") // « /static/css/myStatic.css
                .addResourceLocations("classpath:/") // Default Static Loaction
                .setCachePeriod(1)
                .resourceChain(true) // 4.1
                .addResolver(new GzipResourceResolver()) // 4.1
                .addResolver(new PathResourceResolver()); //4.1

        // src/main/resources/templates/static/...
        registry
                .addResourceHandler("/templates/**") // « /templates/style.css
                .addResourceLocations("classpath:/templates/static/");

        // Do not use the src/main/webapp/... directory if your application is packaged as a jar.
        registry
                .addResourceHandler("/webapp/**") // « /webapp/css/style.css
                .addResourceLocations("/");

        // File located on disk
//        registry
//                .addResourceHandler("/system/files/**")
//                .addResourceLocations("file:///D:/");
    }
}