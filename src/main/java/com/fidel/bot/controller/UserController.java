package com.fidel.bot.controller;


import com.fidel.bot.api.BPMUser;
import com.fidel.bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("configuration/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public BPMUser create(@Validated @RequestBody BPMUser user) {
        return userService.save(user);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<BPMUser> getAll() {
        return userService.getAll();
    }
}