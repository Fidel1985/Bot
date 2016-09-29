package com.fidel.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class MainController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}