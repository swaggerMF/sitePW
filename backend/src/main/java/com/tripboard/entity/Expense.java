package com.tripboard.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Expense extends BaseEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Trip trip;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User createdBy;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User paidBy;

  @Column(nullable = false, length = 200)
  private String description;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private Instant createdAt = Instant.now();

  @OneToMany(
    mappedBy = "expense",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @OrderBy("id")
  private List<ExpenseShare> shares = new ArrayList<>();
}
