package com.coffeeculture.authorizationserver.authentication;

import com.coffeeculture.authorizationserver.models.user.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final JWSAlgorithm jwtAlgorithm = JWSAlgorithm.HS256;
    //@Value("${jwt.secret-key}")
    private final static String SECRET_KEY = "a7rpttGxWI/VAr7+APOfV5Mvaszlb5ynesbF7jlRkuMxquiuNZ+Q08OYfF/wg5EkvLTOV/cE5cTibR2/yerz0Q==";
    private final byte[] sharedSecret = SECRET_KEY.getBytes();

    public String createJwtForUser(User user) {
        try {
            // Set the JWT Claims
            long lifeSpan = 3600 * 1000;
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer("ingela")
                    .issueTime(new Date())
                    .expirationTime(new Date(new Date().getTime() + lifeSpan)) // 1 hour expiration
                    .claim("roles", user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList()
                    )
                    .build();

            // Create HMAC signer
            JWSSigner signer = new MACSigner(sharedSecret);

            // Prepare JWT with claims and sign it
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(jwtAlgorithm), claimsSet);
            signedJWT.sign(signer);

            // Serialize to compact form
            return signedJWT.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JWTClaimsSet decodeJwt(String token) {
        try {
            // Parse the token
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Create HMAC verifier
            JWSVerifier verifier = new MACVerifier(sharedSecret);

            // Verify the signature
            if (!signedJWT.verify(verifier)) {
                throw new JOSEException("Signature verification failed");
            }

            // Retrieve the JWT claims
            return signedJWT.getJWTClaimsSet();
        } catch (JOSEException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Collection<? extends GrantedAuthority> convertAuthorities(List<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}