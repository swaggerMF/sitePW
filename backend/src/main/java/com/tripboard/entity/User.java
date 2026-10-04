package com.tripboard.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
@Entity @Table(name="app_user") @Getter @Setter @NoArgsConstructor
public class User extends BaseEntity {
 @Column(nullable=false,unique=true,length=40) private String username;
 @Column(nullable=false,unique=true,length=254) private String email;
 @Column(nullable=false) private String passwordHash;
 @Column(nullable=false) private Instant createdAt=Instant.now();
}
