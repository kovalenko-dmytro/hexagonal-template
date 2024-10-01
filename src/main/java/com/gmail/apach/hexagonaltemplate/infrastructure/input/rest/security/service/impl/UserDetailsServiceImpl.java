package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.service.impl;

import com.gmail.apach.hexagonaltemplate.application.input.user.GetUserInputPort;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final GetUserInputPort getUserInputPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var user = getUserInputPort.getByUsername(username);
        final var authorities = CollectionUtils.emptyIfNull(user.getRoles()).stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name())))
            .collect(Collectors.toSet());
        return new User(user.getUsername(), user.getPassword(), user.getEnabled(),
            true, true, true, authorities);
    }
}
