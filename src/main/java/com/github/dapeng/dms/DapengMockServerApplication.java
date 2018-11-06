package com.github.dapeng.dms;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@EnableSwagger2Doc
@SpringBootApplication
@PropertySource("classpath:db.properties")
public class DapengMockServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DapengMockServerApplication.class, args);
    }
}
