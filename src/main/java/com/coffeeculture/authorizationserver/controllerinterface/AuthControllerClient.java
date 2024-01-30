package com.coffeeculture.authorizationserver.controllerinterface;

import com.coffeeculture.authorizationserver.authentication.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping()
public interface AuthControllerClient {

    @Operation(
            summary = "Save a new user",
            description = "Save a new user by providing email and password"
    )
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest);

    @Operation(
            summary = "Login existing user",
            description = "Login existing user by providing email and password"
    )
    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody LoginRequest loginRequest);

    @Operation(
            summary = "Test secured end point",
            description = "Returns hello if authenticated"
    )
    @GetMapping()
    String sayHello();
}
