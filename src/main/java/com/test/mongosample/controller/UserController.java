package com.test.mongosample.controller;

import com.test.mongosample.model.User;
import com.test.mongosample.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value= "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public User getUser(@PathVariable String userId) {
        LOG.info("Getting user of ID: {}", userId);
        return userService.getUser(userId);
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public void createNewUser(@RequestBody User user) {
        LOG.info("Creating new user.");
        userService.addUser(user);
    }

    @RequestMapping(value = "/settings/{userId}", method = RequestMethod.GET)
    public Map<String, String> getUserSettings(@RequestParam String userId) {
        return userService.getUserSettings(userId);
    }

    @RequestMapping(value = "/settings/{userId}/{key}")
    public String getSingleUserSetting(@RequestParam String userId, @RequestParam String key) {
        return userService.getSingleUserSetting(userId, key);
    }

    @RequestMapping(value = "/settings/{userId}/{key}/{value}", method = RequestMethod.GET)
    public String addUserSetting(@PathVariable String userId, @PathVariable String key, @PathVariable String value) {
        return userService.addUserSetting(userId, key, value);
    }

}
