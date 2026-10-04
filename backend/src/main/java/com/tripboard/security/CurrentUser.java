package com.tripboard.security;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
@Component
public class CurrentUser {
 public Long id(){return Long.valueOf(((Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSubject());}
}
