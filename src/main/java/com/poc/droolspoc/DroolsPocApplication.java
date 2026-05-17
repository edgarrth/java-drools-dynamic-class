package com.poc.droolspoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.poc.droolspoc.config",
        "com.poc.droolspoc.controller",
        "com.poc.droolspoc.domain",
        "com.poc.droolspoc.service"
})
public class DroolsPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroolsPocApplication.class, args);
    }

}
