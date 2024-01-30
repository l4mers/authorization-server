package com.coffeeculture.authorizationserver.authentication;

import com.coffeeculture.authorizationserver.controllerinterface.AuthControllerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerClient {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.registerLocalUser(loginRequest));
    }

    @Override
    @GetMapping()
    public String sayHello(){
        return "Hello";
    }
}
