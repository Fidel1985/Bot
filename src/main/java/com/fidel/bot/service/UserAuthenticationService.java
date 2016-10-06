package com.fidel.bot.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fidel.bot.jpa.entity.AuthorityEntity;
import com.fidel.bot.jpa.entity.UserEntity;
import com.fidel.bot.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserAuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity entity = getUserEntity(username);
        Set<GrantedAuthority> authorities = entity.getAuthorities().stream()
                .map(AuthorityEntity::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        return new User(entity.getUsername(), entity.getPassword(), authorities);
    }

    private UserEntity getUserEntity(String username) {
        Optional<UserEntity> userEntity = userRepository.getByUsername(username);
        return userEntity.orElseThrow(() ->
                new UsernameNotFoundException("Entered combination of Username and Password is incorrect."));
    }
}
