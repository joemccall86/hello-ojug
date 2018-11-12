package com.example.dockerlogindemo.services;

import com.example.dockerlogindemo.domain.RegistrationCode;
import com.example.dockerlogindemo.domain.WebUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class PasswordRecoveryService {

    private static final Logger log = LoggerFactory.getLogger(PasswordRecoveryService.class);

    private final MailSender mailSender;
    private final RegistrationCodeService registrationCodeService;
    private final WebUserService webUserService;

    @Autowired
    public PasswordRecoveryService(MailSender mailSender, RegistrationCodeService registrationCodeService, WebUserService webUserService) {
        this.mailSender = mailSender;
        this.registrationCodeService = registrationCodeService;
        this.webUserService = webUserService;
    }

    public void sendForgotPasswordEmail(String email) {

        // Create the registration code for this user
        RegistrationCode registrationCode = new RegistrationCode(email);
        registrationCodeService.save(registrationCode);

        String link = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("reset-password")
                .queryParam("token", registrationCode.getToken())
                .build().toString();

        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom("admin@example.com");
        msg.setTo(email);
        msg.setSubject("Reset Password");
        msg.setText(
                "Your user with email " + email + " has requested a password reset. If you did not request this " +
                        "please disregard this email. Otherwise click the following link: " + link
        );

        try {
            mailSender.send(msg);
            log.debug("Sent forgot password email to " + email);

        } catch (MailException e) {
            log.error("When sending reset password message", e);

        }
    }

    public void setNewPassword(String username, String password) {
        Optional<WebUser> user = webUserService.findByUsername(username);
        user.ifPresent(webUser -> {
            webUser.setPassword(password);
            webUserService.save(webUser);

            log.debug("Reset password success for user " + username);

        });
    }
}
