package com.coffeeculture.authorizationserver.models.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_credentials")
public class UserCredentials {
    @Id
    @Column(name = "credential_id")
    private String id;
    @Column(name = "password_hash")
    private String passwordHash;
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //---- Relations ----//
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
