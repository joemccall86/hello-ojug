package com.example.dockerlogindemo;

import com.example.dockerlogindemo.domain.WebUser;
import com.example.dockerlogindemo.services.WebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BootStrap {

    final WebUserService webUserService;

    @Autowired
    public BootStrap(WebUserService webUserService) {
        this.webUserService = webUserService;
    }

    @PostConstruct
    void init() {
        if (webUserService.countByUsername("user1@example.com") == 0) {
            webUserService.save(new WebUser("user1@example.com", "password"));
            webUserService.save(new WebUser("user2@example.com", "password"));
        }
    }
}
