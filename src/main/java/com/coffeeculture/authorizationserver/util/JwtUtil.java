package com.coffeeculture.authorizationserver.util;

import com.coffeeculture.authorizationserver.models.user.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
@Component
public class JwtUtil {

    //private final String SECRET = "123";
    private static final JWSAlgorithm jwtAlgorithm = JWSAlgorithm.HS256;
    private static final byte[] sharedSecret = "Your-Secret-Key".getBytes();
    private static final long lifeSpan = 3600 * 1000;

    public static String createJwtForUser(User user) {
        try {
            // Set the JWT Claims
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUserId())
                    .issuer("your-app-name")
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

    public static JWTClaimsSet decodeJwt(String token) {
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
}