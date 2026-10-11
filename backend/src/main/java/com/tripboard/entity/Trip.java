package com.tripboard.entity;
import jakarta.persistence.*;import lombok.*;import java.time.*;
@Entity @Getter @Setter @NoArgsConstructor
public class Trip extends BaseEntity {
 @Column(nullable=false,length=100) private String name;
 @Column(nullable=false,length=150) private String destination;
 @Column(nullable=false,length=2000) private String description;
 @Column(nullable=false) private LocalDate startDate;
 @Column(nullable=false) private LocalDate endDate;
 @Column(nullable=false,length=3) private String currency;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private User owner;
 @Column(nullable=false) private Instant createdAt=Instant.now();
}
