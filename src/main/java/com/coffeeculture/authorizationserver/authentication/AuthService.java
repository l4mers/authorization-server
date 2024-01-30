package com.coffeeculture.authorizationserver.authentication;

import com.coffeeculture.authorizationserver.models.user.*;
import com.coffeeculture.authorizationserver.util.DefaultIdProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthService {
    //--- utils ---//
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final DefaultIdProvider defaultIdProvider;
    private final JwtService jwtService;

    //--- repos ---//
    private final UserRepository userRepo;
    private final UserCredentialsRepository userCredentialsRepo;
    private final RoleRepository roleRepo;

    private Authentication authentication(String username, String password){
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
    }

    public AuthResponse login(LoginRequest loginRequest){
        Authentication authentication = authentication(
                loginRequest.getUsername(),
                loginRequest.getPassword());

        User user = (User) authentication.getPrincipal();
        user.setLastLogin(LocalDateTime.now());

        return AuthResponse.builder()
                .jwt(jwtService.createJwtForUser(userRepo.save(user)))
                .build();
    }
    @Transactional
    public AuthResponse registerLocalUser(LoginRequest loginRequest) {

        Role role = roleRepo.findByRole("USER")
                .orElseGet(() -> roleRepo.save(Role.builder().role("USER").build()));

        User user = userRepo.save(User.builder()
                        .userId(defaultIdProvider.getRandomId())
                        .email(loginRequest.getUsername())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .lastLogin(LocalDateTime.now())
                .build());

        userCredentialsRepo.save(
                UserCredentials.builder()
                        .id(defaultIdProvider.getRandomId())
                        .passwordHash(passwordEncoder.encode(loginRequest.getPassword()))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .user(user)
                        .build());

        user.setUserRoles(new HashSet<>());
        user.getUserRoles().add(role);
        return AuthResponse.builder()
                .jwt(jwtService.createJwtForUser(userRepo.save(user)))
                .build();
    }
}
