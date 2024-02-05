package com.example.best_travel.infraestructure.service;

import com.example.best_travel.domain.entities.documents.AppUserDocument;
import com.example.best_travel.domain.repositories.mongo.AppUserRepository;
import com.example.best_travel.infraestructure.abstrat.ModifyUserService;
import com.example.best_travel.util.exceptions.UsernameNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class AppUserService implements ModifyUserService, UserDetailsService {

    private final AppUserRepository userRepository;

    private static final String collectioName = "app_users";

    @Override
    public Map<String, Boolean> enabled(String username) {
        var user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(collectioName));
        user.setEnabled(!user.isEnabled());
        var userSaved = this.userRepository.save(user);
        return Collections.singletonMap(userSaved.getUsername(), userSaved.isEnabled());
    }

    @Override
    public Map<String, Set<String>> addRole(String role, String username) {
        var user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(collectioName));

        user.getRole().getGrantedAuthorities().add(role);
        var userSaved = this.userRepository.save(user);
        var authorities = userSaved.getRole().getGrantedAuthorities();
       log.info("Users {} add role {}", userSaved.getUsername(), userSaved.getRole().getGrantedAuthorities().toString());
        return Collections.singletonMap(userSaved.getUsername(), authorities);
    }

    @Override
    public Map<String, Set<String>> removeRole(String role, String username) {
        var user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(collectioName));

        user.getRole().getGrantedAuthorities().remove(role);
        var userSaved = this.userRepository.save(user);
        var authorities = userSaved.getRole().getGrantedAuthorities();
        log.info("Users {} remove role {}", userSaved.getUsername(), userSaved.getRole().getGrantedAuthorities().toString());
        return Collections.singletonMap(userSaved.getUsername(), authorities);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username){
        var user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(collectioName));

        return mapUserToDetail(user);

    }

    private static UserDetails mapUserToDetail(AppUserDocument appUserDocument){
        Set<GrantedAuthority> authorities = appUserDocument.getRole()
                .getGrantedAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        
        return new User(
                appUserDocument.getUsername(),
                appUserDocument.getPassword(),
                appUserDocument.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }

}
