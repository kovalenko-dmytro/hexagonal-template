package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.output.user.CreateUserOutputPort;
import com.gmail.apach.jenkins_demo.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.jenkins_demo.common.dto.CurrentUserContext;
import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.common.util.CurrentUserContextUtil;
import com.gmail.apach.jenkins_demo.data.AuthoritiesTestData;
import com.gmail.apach.jenkins_demo.data.CreateUserTestData;
import com.gmail.apach.jenkins_demo.domain.email.model.EmailType;
import com.gmail.apach.jenkins_demo.domain.email.wrapper.SendEmailWrapper;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
import com.gmail.apach.jenkins_demo.domain.user.model.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.validator.CreateUserPermissionsValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    private static final String ENCODED_PASSWORD = "encodedPassword";

    @InjectMocks
    private CreateUserService createUserService;
    @Mock
    private CreateUserOutputPort createUserOutputPort;
    @Mock
    private CreateUserPermissionsValidator createUserPermissionsValidator;
    @Mock
    private CurrentUserContextUtil currentUserContextUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void createUser_success() {
        final var user = CreateUserTestData.user();
        final var savedUser = CreateUserTestData.savedUser();
        final var context = CurrentUserContext.builder()
            .username("admin")
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();
        final var emailData = SendEmailWrapper.builder()
            .sendBy(context.username())
            .sendTo(savedUser.getEmail())
            .subject("Hello")
            .properties(Map.of(
                EmailType.Property.RECIPIENT_NAME.getProperty(), savedUser.getUsername(),
                EmailType.Property.SENDER_NAME.getProperty(), context.username()
            ))
            .emailType(EmailType.INVITE)
            .build();

        when(passwordEncoder.encode(user.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(currentUserContextUtil.getContext()).thenReturn(context);
        doNothing().when(createUserPermissionsValidator).validate(user.getRoles());
        doNothing().when(rabbitTemplate).convertAndSend(EmailProcessingConfig.EMAIL_DIRECT_EXCHANGE,
            EmailProcessingConfig.EMAIL_ROUTING_KEY, emailData);
        when(createUserOutputPort.createUser(user)).thenReturn(savedUser);

        final var actual = createUserService.createUser(user);

        assertNotNull(actual);
        assertNotNull(actual.getUserId());
    }

    @Test
    void createUserWithNoRoles_success() {
        final var user = CreateUserTestData.user();
        final var userWithNoRoles = CreateUserTestData.userWithNoRoles();
        final var savedUser = CreateUserTestData.savedUser();
        final var context = CurrentUserContext.builder()
            .username("admin")
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();
        final var emailData = SendEmailWrapper.builder()
            .sendBy(context.username())
            .sendTo(savedUser.getEmail())
            .subject("Hello")
            .properties(Map.of(
                EmailType.Property.RECIPIENT_NAME.getProperty(), savedUser.getUsername(),
                EmailType.Property.SENDER_NAME.getProperty(), context.username()
            ))
            .emailType(EmailType.INVITE)
            .build();

        when(passwordEncoder.encode(user.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(currentUserContextUtil.getContext()).thenReturn(context);
        doNothing().when(rabbitTemplate).convertAndSend(EmailProcessingConfig.EMAIL_DIRECT_EXCHANGE,
            EmailProcessingConfig.EMAIL_ROUTING_KEY, emailData);
        when(createUserOutputPort.createUser(userWithNoRoles)).thenReturn(savedUser);

        final var actual = createUserService.createUser(userWithNoRoles);

        assertNotNull(actual);
        assertNotNull(actual.getUserId());
        assertFalse(actual.getRoles().isEmpty());
        assertEquals(1, actual.getRoles().size());
        final var roleTypes = actual.getRoles().stream().map(Role::getRole).collect(Collectors.toSet());
        assertTrue(roleTypes.contains(RoleType.USER));
    }

    @Test
    void createUser_forbidden() {
        final var user = CreateUserTestData.user();
        final var context = CurrentUserContext.builder()
            .username("user")
            .authorities(AuthoritiesTestData.userAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);
        doThrow(new ForbiddenException("forbidden"))
            .when(createUserPermissionsValidator).validate(user.getRoles());

        assertThrows(ForbiddenException.class, () -> createUserService.createUser(user));
    }
}