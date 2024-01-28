package com.example.best_travel.infraestructure.service;

import com.example.best_travel.domain.repositories.mongo.AppUserRepository;
import com.example.best_travel.infraestructure.abstrat.ModifyUserService;
import com.example.best_travel.util.exceptions.UsernameNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class AppUserService implements ModifyUserService {

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
}
