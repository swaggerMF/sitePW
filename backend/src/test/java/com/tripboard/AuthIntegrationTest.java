package com.tripboard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class AuthIntegrationTest {
 @Autowired MockMvc mvc;
 @Test void registrationLoginAndProtectedRoutes() throws Exception {
  mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
  mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"a-test-password\"}"))
   .andExpect(status().isCreated()).andExpect(jsonPath("$.token").isString()).andExpect(jsonPath("$.user.passwordHash").doesNotExist());
  mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"test@example.com\",\"password\":\"a-test-password\"}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.user.username").value("testuser"));
  mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"test@example.com\",\"password\":\"wrong\"}"))
   .andExpect(status().isUnauthorized());
 }
}
