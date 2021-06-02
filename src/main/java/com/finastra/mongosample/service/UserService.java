package com.finastra.mongosample.service;

import com.finastra.mongosample.model.User;
import com.finastra.mongosample.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        if(user != null) {
            userRepository.save(user);
            LOG.info("User successfully saved");
        }
    }

    public User getUser(String userId) {
        if(userId == null) {
            throw new IllegalArgumentException("UserId is empty.");
        }
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(IllegalArgumentException::new);
    }

    public List<User> findAllUsers() {
        LOG.info("Getting all users.");
        return userRepository.findAll();
    }

    public Map<String, String> getUserSettings(String userId) {
        LOG.info("Getting settings for user: {}", userId);
        User user = getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("User does not exist.");
        } else {
            return user.getUserSettings();
        }
    }

    public String getSingleUserSetting(String userId, String key) {
        LOG.info("Getting setting: {} for user: {}", key, userId);
        User user = getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("User does not exist.");
        } else {
            return user.getUserSettings().get(key);
        }
    }

    public String addUserSetting(String userId, String key, String value) {
        LOG.info("Adding setting: {} = {} for user: {}", key, value, userId);
        User user = getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("User does not exist.");
        } else {
            user.getUserSettings().put(key, value);
            return "Entry added";
        }
    }

}
