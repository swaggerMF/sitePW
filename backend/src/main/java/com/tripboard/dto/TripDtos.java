package com.tripboard.dto;

import jakarta.validation.constraints.*;
import java.time.*;

public final class TripDtos {

  public record Input(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Size(max = 150) String destination,
    @Size(max = 2000) String description,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    @NotBlank @Pattern(regexp = "EUR|RON|USD") String currency
  ) {}

  public record View(
    Long id,
    String name,
    String destination,
    String description,
    LocalDate startDate,
    LocalDate endDate,
    String currency,
    Long ownerId,
    long memberCount,
    Instant createdAt
  ) {}

  public record Member(Long id, String username, String email, String role) {}

  public record AddMember(@NotBlank @Size(max = 254) String query) {}
}
