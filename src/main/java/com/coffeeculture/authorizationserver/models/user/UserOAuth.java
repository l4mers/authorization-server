package com.coffeeculture.authorizationserver.models.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_oauth")
public class UserOAuth {
    @Id
    @Column(name = "oauth_id")
    private String oauthId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_user_id")
    private String providerUserId;

    @Column(name = "provider_access_token")
    private String providerAccessToken;

    @Column(name = "provider_refresh_token")
    private String providerRefreshToken;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}
