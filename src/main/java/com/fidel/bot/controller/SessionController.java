package com.fidel.bot.controller;


import com.fidel.bot.api.BPMUser;
import com.fidel.bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/session")
public class SessionController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public BPMUser currentUser(@AuthenticationPrincipal User user) {
        BPMUser bpmUser = userService.getByUsername(user.getUsername());
        bpmUser.setPassword(null);
        return bpmUser;
    }

}
