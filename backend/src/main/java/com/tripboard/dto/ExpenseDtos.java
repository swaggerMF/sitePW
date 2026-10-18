package com.tripboard.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;

public final class ExpenseDtos {

  public record Input(
    @NotBlank @Size(max = 200) String description,
    @NotNull @DecimalMin("0.01") @Digits(
      integer = 10,
      fraction = 2
    ) BigDecimal amount,
    @NotNull Long paidById,
    @NotNull LocalDate date,
    @NotEmpty @Size(max = 100) List<@NotNull Long> participantIds
  ) {}

  public record Share(Long userId, String username, BigDecimal amount) {}

  public record View(
    Long id,
    String description,
    BigDecimal amount,
    Long paidById,
    String paidBy,
    LocalDate date,
    Long createdById,
    List<Share> shares
  ) {}
}
