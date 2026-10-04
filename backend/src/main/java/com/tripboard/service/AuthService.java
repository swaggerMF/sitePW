package com.tripboard.service;
import com.tripboard.dto.AuthDtos.*;
import com.tripboard.entity.User;
import com.tripboard.repository.UserRepository;
import com.tripboard.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import java.time.Instant;
import java.util.Locale;
@Service @RequiredArgsConstructor
public class AuthService {
 private final UserRepository users; private final PasswordEncoder passwords; private final JwtEncoder encoder;
 @Transactional public Session register(Register input){
  String email=input.email().trim().toLowerCase(Locale.ROOT);
  if(users.existsByEmailIgnoreCase(email)||users.existsByUsernameIgnoreCase(input.username())) throw ApiException.bad("Username or email is already registered.");
  if(input.password().getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72) throw ApiException.bad("Password must be at most 72 UTF-8 bytes.");
  var user=new User();user.setUsername(input.username());user.setEmail(email);user.setPasswordHash(passwords.encode(input.password()));return session(users.save(user));
 }
 public Session login(Login input){
  var user=users.findByEmailIgnoreCase(input.email().trim()).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Incorrect email or password."));
  if(!passwords.matches(input.password(),user.getPasswordHash())) throw new ApiException(HttpStatus.UNAUTHORIZED,"Incorrect email or password.");return session(user);
 }
 public UserView me(Long id){return view(users.findById(id).orElseThrow(ApiException::missing));}
 private Session session(User user){var now=Instant.now();var claims=JwtClaimsSet.builder().issuer("tripboard").subject(user.getId().toString()).issuedAt(now).expiresAt(now.plusSeconds(28800)).build();return new Session(encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims)).getTokenValue(),view(user));}
 private UserView view(User u){return new UserView(u.getId(),u.getUsername(),u.getEmail());}
}
