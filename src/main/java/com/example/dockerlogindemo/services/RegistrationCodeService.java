package com.example.dockerlogindemo.services;

import com.example.dockerlogindemo.domain.RegistrationCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RegistrationCodeService extends CrudRepository<RegistrationCode, Long> {

    Optional<RegistrationCode> findByToken(String token);
}
