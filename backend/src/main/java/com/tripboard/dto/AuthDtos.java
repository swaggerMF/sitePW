package com.tripboard.dto;
import jakarta.validation.constraints.*;
public final class AuthDtos {
 public record Register(@NotBlank @Pattern(regexp="[a-zA-Z0-9_]{3,40}") String username,
  @NotBlank @Email @Size(max=254) String email,@NotBlank @Size(min=10,max=72) String password) {}
 public record Login(@NotBlank @Email String email,@NotBlank @Size(max=72) String password) {}
 public record UserView(Long id,String username,String email) {}
 public record Session(String token,UserView user) {}
}
