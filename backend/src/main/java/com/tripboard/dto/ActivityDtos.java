package com.tripboard.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;

public final class ActivityDtos {

  public record Input(
    @NotBlank @Size(max = 120) String title,
    @Size(max = 2000) String description,
    @Size(max = 200) String location,
    @NotNull LocalDate date,
    @NotNull LocalTime startTime,
    @NotNull @DecimalMin("0.00") @Digits(
      integer = 10,
      fraction = 2
    ) BigDecimal estimatedCost
  ) {}

  public record View(
    Long id,
    String title,
    String description,
    String location,
    LocalDate date,
    LocalTime startTime,
    BigDecimal estimatedCost,
    Long createdById,
    String createdBy
  ) {}
}
