package com.coffeeculture.authorizationserver.models.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String> {
}
