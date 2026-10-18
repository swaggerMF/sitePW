package com.tripboard.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;

public final class SuggestionDtos {

  public record Input(
    @NotBlank @Size(max = 120) String title,
    @Size(max = 2000) String description,
    @Size(max = 200) String location,
    @NotNull @DecimalMin("0.00") @Digits(
      integer = 10,
      fraction = 2
    ) BigDecimal estimatedCost
  ) {}

  public record VoteInput(
    @NotBlank @Pattern(regexp = "UP|DOWN") String value
  ) {}

  public record Convert(
    @NotNull LocalDate date,
    @NotNull LocalTime startTime
  ) {}

  public record View(
    Long id,
    String title,
    String description,
    String location,
    BigDecimal estimatedCost,
    String status,
    Long createdById,
    String createdBy,
    long upVotes,
    long downVotes,
    String myVote,
    boolean converted,
    Long activityId,
    Instant createdAt
  ) {}
}
