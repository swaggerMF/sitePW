package com.tripboard;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tripboard.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityValidationTest extends ApiTestSupport {

  @Autowired
  JwtEncoder encoder;

  @Autowired
  UserRepository users;

  @Test
  void outsiderCannotReadOrMutateNestedResources() throws Exception {
    String owner = register("securityowner"),
      outsider = register("securityoutsider");
    long trip = trip(owner);
    long activity = postJson(
      "/api/trips/" + trip + "/activities",
      owner,
      Map.of(
        "title",
        "Stop",
        "date",
        "2027-07-12",
        "startTime",
        "10:00",
        "estimatedCost",
        "0"
      ),
      201
    )
      .get("id")
      .asLong();
    long idea = postJson(
      "/api/trips/" + trip + "/suggestions",
      owner,
      Map.of("title", "Idea", "estimatedCost", "5.00"),
      201
    )
      .get("id")
      .asLong();
    long ownerId = users
      .findByUsernameIgnoreCase("securityowner")
      .orElseThrow()
      .getId();
    long expense = postJson(
      "/api/trips/" + trip + "/expenses",
      owner,
      Map.of(
        "description",
        "Food",
        "amount",
        "10",
        "paidById",
        ownerId,
        "date",
        "2027-07-12",
        "participantIds",
        List.of(ownerId)
      ),
      201
    )
      .get("id")
      .asLong();
    for (String resource : List.of(
      "activities/" + activity,
      "suggestions/" + idea,
      "expenses/" + expense
    )) {
      mvc
        .perform(
          get("/api/" + resource).header("Authorization", "Bearer " + outsider)
        )
        .andExpect(status().isForbidden());
      mvc
        .perform(
          delete("/api/" + resource).header(
            "Authorization",
            "Bearer " + outsider
          )
        )
        .andExpect(status().isForbidden());
    }
    for (String section : List.of(
      "activities",
      "expenses",
      "members",
      "suggestions",
      "settlements",
      "balances",
      "dashboard"
    )) {
      mvc
        .perform(
          get("/api/trips/" + trip + "/" + section).header(
            "Authorization",
            "Bearer " + outsider
          )
        )
        .andExpect(status().isForbidden());
    }
    postJson(
      "/api/suggestions/" + idea + "/vote",
      outsider,
      Map.of("value", "UP"),
      403
    );
    postJson(
      "/api/trips/" + trip + "/settlements",
      owner,
      Map.of(
        "fromUserId",
        ownerId,
        "toUserId",
        ownerId,
        "amount",
        "10",
        "date",
        "2027-07-12"
      ),
      400
    );
    postJson(
      "/api/trips/" + trip + "/expenses",
      owner,
      Map.of(
        "description",
        "Invalid",
        "amount",
        "0",
        "paidById",
        ownerId,
        "date",
        "2027-07-12",
        "participantIds",
        List.of(ownerId)
      ),
      400
    );
    postJson(
      "/api/trips/" + trip + "/expenses",
      owner,
      Map.of(
        "description",
        "Invalid",
        "amount",
        "1.001",
        "paidById",
        ownerId,
        "date",
        "2027-07-12",
        "participantIds",
        List.of(ownerId)
      ),
      400
    );
    mvc
      .perform(
        get("/api/trips/999999").header("Authorization", "Bearer " + owner)
      )
      .andExpect(status().isNotFound());
    mvc
      .perform(
        delete("/api/trips/" + trip).header("Authorization", "Bearer " + owner)
      )
      .andExpect(status().isNoContent());
    assertThat(users.findById(ownerId)).isPresent();
  }

  @Test
  void invalidAndExpiredTokensFailAndPasswordsAreHashed() throws Exception {
    register("securitypassword");
    var user = users.findByUsernameIgnoreCase("securitypassword").orElseThrow();
    assertThat(user.getPasswordHash())
      .startsWith("$2")
      .isNotEqualTo("test-password-123");
    mvc
      .perform(
        get("/api/trips").header(
          "Authorization",
          "Bearer tampered.token.signature"
        )
      )
      .andExpect(status().isUnauthorized());
    var claims = JwtClaimsSet.builder()
      .issuer("tripboard")
      .subject(user.getId().toString())
      .issuedAt(Instant.now().minusSeconds(600))
      .expiresAt(Instant.now().minusSeconds(300))
      .build();
    String expired = encoder
      .encode(
        JwtEncoderParameters.from(
          JwsHeader.with(MacAlgorithm.HS256).build(),
          claims
        )
      )
      .getTokenValue();
    mvc
      .perform(get("/api/trips").header("Authorization", "Bearer " + expired))
      .andExpect(status().isUnauthorized());
    mvc
      .perform(
        post("/api/auth/login")
          .contentType("application/json")
          .content(
            json.writeValueAsString(
              Map.of(
                "email",
                "securitypassword@example.com",
                "password",
                "é".repeat(40)
              )
            )
          )
      )
      .andExpect(status().isUnauthorized());
    mvc
      .perform(
        post("/api/auth/register")
          .contentType("application/json")
          .content(
            json.writeValueAsString(
              Map.of(
                "username",
                "unicodepassword",
                "email",
                "unicodepassword@example.com",
                "password",
                "é".repeat(40)
              )
            )
          )
      )
      .andExpect(status().isBadRequest());
  }
}
