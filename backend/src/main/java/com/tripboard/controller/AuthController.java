package com.tripboard.controller;
import com.tripboard.dto.AuthDtos.*;
import com.tripboard.service.AuthService;
import com.tripboard.security.CurrentUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
 private final AuthService auth; private final CurrentUser current;
 @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public Session register(@Valid @RequestBody Register body){return auth.register(body);}
 @PostMapping("/login") public Session login(@Valid @RequestBody Login body){return auth.login(body);}
 @GetMapping("/me") public UserView me(){return auth.me(current.id());}
}
