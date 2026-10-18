package com.tripboard;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TripAccessTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper json;

  String register(String name) throws Exception {
    return json
      .readTree(
        mvc
          .perform(
            post("/api/auth/register")
              .contentType(MediaType.APPLICATION_JSON)
              .content(
                "{\"username\":\"" +
                  name +
                  "\",\"email\":\"" +
                  name +
                  "@example.com\",\"password\":\"test-password-123\"}"
              )
          )
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString()
      )
      .get("token")
      .asText();
  }

  @Test
  void onlyMembersReadAndOnlyOwnerManagesTrip() throws Exception {
    String owner = register("owneraccess"),
      other = register("otheraccess");
    String body =
      "{\"name\":\"Iceland\",\"destination\":\"Iceland\",\"description\":\"Friends\",\"startDate\":\"2027-07-12\",\"endDate\":\"2027-07-18\",\"currency\":\"EUR\"}";
    long id = json
      .readTree(
        mvc
          .perform(
            post("/api/trips")
              .header("Authorization", "Bearer " + owner)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
          )
          .andExpect(status().isCreated())
          .andReturn()
          .getResponse()
          .getContentAsString()
      )
      .get("id")
      .asLong();
    mvc
      .perform(
        get("/api/trips/" + id).header("Authorization", "Bearer " + other)
      )
      .andExpect(status().isForbidden());
    mvc
      .perform(
        post("/api/trips/" + id + "/members")
          .header("Authorization", "Bearer " + owner)
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"query\":\"otheraccess\"}")
      )
      .andExpect(status().isCreated());
    mvc
      .perform(
        get("/api/trips/" + id).header("Authorization", "Bearer " + other)
      )
      .andExpect(status().isOk());
    mvc
      .perform(
        put("/api/trips/" + id)
          .header("Authorization", "Bearer " + other)
          .contentType(MediaType.APPLICATION_JSON)
          .content(body)
      )
      .andExpect(status().isForbidden());
    mvc
      .perform(
        delete("/api/trips/" + id).header("Authorization", "Bearer " + other)
      )
      .andExpect(status().isForbidden());
    mvc
      .perform(
        delete("/api/trips/" + id).header("Authorization", "Bearer " + owner)
      )
      .andExpect(status().isNoContent());
    mvc
      .perform(get("/api/auth/me").header("Authorization", "Bearer " + other))
      .andExpect(status().isOk());
  }
}
