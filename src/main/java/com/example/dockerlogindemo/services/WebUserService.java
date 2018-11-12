package com.example.dockerlogindemo.services;

import com.example.dockerlogindemo.domain.WebUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WebUserService extends CrudRepository<WebUser, Long> {

    long countByUsername(String username);
    Optional<WebUser> findByUsername(String username);
}
