package com.fidel.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {

    //@Value("${url.main-app}")
    //private String mainAppBaseURL;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

/*    @Bean
    public RestTemplate mainAppCient() {
        //Settings settings = settings().get();
        return new RestTemplateBuilder()
                .rootUri(mainAppBaseURL)
                //.basicAuthorization(settings.getUserName(), settings.getPassword())
                .setConnectTimeout(5000)
                .setReadTimeout(5000)
                .build();
    }*/

}
