package com.tripboard.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Suggestion extends BaseEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Trip trip;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User createdBy;

  @Column(nullable = false, length = 120)
  private String title;

  @Column(nullable = false, length = 2000)
  private String description;

  @Column(nullable = false, length = 200)
  private String location;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal estimatedCost;

  @Column(nullable = false, length = 10)
  private String status = "OPEN";

  @Column(nullable = false)
  private Instant createdAt = Instant.now();

  @Column(nullable = false)
  private boolean converted = false;

  @OneToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private Activity activity;
}
