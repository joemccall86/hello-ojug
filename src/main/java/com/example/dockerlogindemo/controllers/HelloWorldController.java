package com.example.dockerlogindemo.controllers;

import com.example.dockerlogindemo.domain.RegistrationCode;
import com.example.dockerlogindemo.services.PasswordRecoveryService;
import com.example.dockerlogindemo.services.RegistrationCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class HelloWorldController {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldController.class);

    private final PasswordRecoveryService passwordRecoveryService;
    private final RegistrationCodeService registrationCodeService;

    @Autowired
    public HelloWorldController(PasswordRecoveryService passwordRecoveryService, RegistrationCodeService registrationCodeService) {
        this.passwordRecoveryService = passwordRecoveryService;
        this.registrationCodeService = registrationCodeService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(String username, ModelMap modelMap) {

        passwordRecoveryService.sendForgotPasswordEmail(username);
        modelMap.addAttribute("username", username);
        return "email-sent";
    }

    @GetMapping("/reset-password")
    public String resetPassword(String token, ModelMap modelMap) {

        Optional<RegistrationCode> registrationCodeOptional = registrationCodeService.findByToken(token);

        if (registrationCodeOptional.isPresent()) {
            modelMap.put("username", registrationCodeOptional.get().getUsername());
            return "reset-password";

        } else {
            // Token does not exist, so ignore it
            return "redirect:/login";

        }
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(String username, String password, String passwordConfirmation) {
        if (password.equals(passwordConfirmation)) {
            passwordRecoveryService.setNewPassword(username, password);

        }

        return "redirect:/login";
    }
}
