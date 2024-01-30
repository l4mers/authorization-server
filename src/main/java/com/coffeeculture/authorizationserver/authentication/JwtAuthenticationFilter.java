package com.coffeeculture.authorizationserver.authentication;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. Extract JWT from the request
        String jwtToken = extractJwtFromRequest(request);

        if (jwtToken != null) {
            // 2. Decode the JWT and retrieve claims
            JWTClaimsSet claims = jwtService.decodeJwt(jwtToken);

            //Date expirationTime = claims.getExpirationTime();

            if (claims != null) {
                // 3. Extract username and roles from the JWT claims
                String username = claims.getSubject();
                // 4. Create authentication object
                if (claims.getSubject() != null) {
                    //Authentication auth = new UsernamePasswordAuthenticationToken(username, null, JwtUtil.convertAuthorities(roles));
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, jwtService.convertAuthorities(
                            (List<String>) claims.getClaim("roles")
                    ));
                    // 5. Set the authentication in the security context
                    // Set the authentication details
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set the authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);

    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
