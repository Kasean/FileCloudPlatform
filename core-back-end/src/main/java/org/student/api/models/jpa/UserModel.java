package org.student.api.models.jpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(name = "password",length = 100,nullable = false)
    private String password;

    @Column(name = "name",nullable = false,unique = true,length = 50)
    private String name;

    @Column(name = "email",nullable = false,unique = true,length = 100)
    private String email;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;
}
