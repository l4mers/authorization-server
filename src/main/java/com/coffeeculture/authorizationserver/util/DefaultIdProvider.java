package com.coffeeculture.authorizationserver.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DefaultIdProvider {
    public String getRandomId(){
        return UUID.randomUUID().toString();
    }
}
