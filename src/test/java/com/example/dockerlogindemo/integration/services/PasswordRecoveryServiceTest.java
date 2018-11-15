package com.example.dockerlogindemo.integration.services;

import com.example.dockerlogindemo.DockerLoginDemoApplication;
import com.example.dockerlogindemo.domain.WebUser;
import com.example.dockerlogindemo.services.PasswordRecoveryService;
import com.example.dockerlogindemo.services.WebUserService;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.mail.*;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DockerLoginDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PasswordRecoveryServiceTest {

    public static final String EMAIL_CONTENT_SUBSTRING = "has requested a password reset. If you did not request this ";
    @Rule
    public PostgreSQLContainer postgresql = new PostgreSQLContainer("postgres:10.5-alpine");

    @ClassRule
    static public GenericContainer greenmail = new FixedHostPortGenericContainer("greenmail/standalone:1.5.8")
            .withFixedExposedPort(3025, 3025);

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    @Autowired
    private WebUserService webUserService;

    @Test
    public void testChangePassword() {

        // Given
        String username = "user1@example.com";
        String newPassword= UUID.randomUUID().toString();

        // When
        passwordRecoveryService.setNewPassword(username, newPassword);

        // Then
        Optional<WebUser> foundUser = webUserService.findByUsername(username);

        assertTrue(foundUser.isPresent());

        assertEquals(newPassword, foundUser.get().getPassword());
    }

    @Test
    public void testReceiveEmail() throws MessagingException, IOException {

        String username = "user1@example.com";

        // Given the user requests a new password
        passwordRecoveryService.sendForgotPasswordEmail(username);

        // When: setup the local email retrieval
        Properties props = new Properties();
        props.setProperty("mail.pop3.connectiontimeout", "5000");
        Session session = Session.getInstance(props);
        URLName urlName = new URLName("pop3", "localhost", greenmail.getMappedPort(3110), null, username, "doesnotmatter");
        Store store = session.getStore(urlName);
        store.connect();

        // Then
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();
        assertNotNull(messages);
        assertThat(1, equalTo(messages.length));
        assertEquals("Reset Password", messages[0].getSubject());
        assertEquals("admin@example.com", messages[0].getFrom()[0].toString());
        assertNotNull(messages[0].getContent());
        assertThat(messages[0].getContent().toString(), containsString(EMAIL_CONTENT_SUBSTRING));
    }
}