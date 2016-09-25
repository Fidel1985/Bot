package com.fidel.bot.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {

    @Value("${url.main-app}")
    private String mainAppBaseURL;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

/*        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }*/
    }

    @Bean
    public RestTemplate mainAppCient() {
        //Settings settings = settings().get();
        return new RestTemplateBuilder()
                .rootUri(mainAppBaseURL)
                //.basicAuthorization(settings.getUserName(), settings.getPassword())
                .setConnectTimeout(5000)
                .setReadTimeout(5000)
                .build();
    }

}
