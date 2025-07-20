package com.apis.postgrestesting.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "urlssss")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long urlId;

    @Column(nullable = false)
    private String urlName;

    private String urlDescription;

    private String urlCategory;

    @Column(nullable = false)
    private String urlLink;

    @Column(name = "user_id", nullable = false)
    private Long userId;
    // Just a user ID, not a relationship

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
