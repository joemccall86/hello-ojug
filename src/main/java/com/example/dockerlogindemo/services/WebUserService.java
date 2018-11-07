package com.example.dockerlogindemo.services;

import com.example.dockerlogindemo.domain.WebUser;
import org.springframework.data.repository.CrudRepository;

public interface WebUserService extends CrudRepository<WebUser, Long> {

    long countByUsername(String username);
}
