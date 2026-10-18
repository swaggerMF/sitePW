package com.tripboard;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ActivityIntegrationTest extends ApiTestSupport {

  @Test
  void activityCrudChecksTripDatesAndOwnership() throws Exception {
    String owner = register("activityowner"),
      member = register("activitymember");
    long trip = trip(owner);
    postJson(
      "/api/trips/" + trip + "/members",
      owner,
      Map.of("query", "activitymember"),
      201
    );
    var body = Map.of(
      "title",
      "Waterfall",
      "date",
      "2027-07-13",
      "startTime",
      "09:00",
      "estimatedCost",
      "0.00"
    );
    long id = postJson("/api/trips/" + trip + "/activities", owner, body, 201)
      .get("id")
      .asLong();
    mvc
      .perform(
        delete("/api/activities/" + id).header(
          "Authorization",
          "Bearer " + member
        )
      )
      .andExpect(status().isForbidden());
    postJson(
      "/api/trips/" + trip + "/activities",
      member,
      Map.of(
        "title",
        "Outside dates",
        "date",
        "2028-01-01",
        "startTime",
        "09:00",
        "estimatedCost",
        "0"
      ),
      400
    );
    mvc
      .perform(
        get("/api/activities/" + id).header("Authorization", "Bearer " + member)
      )
      .andExpect(status().isOk());
    mvc
      .perform(
        put("/api/activities/" + id)
          .header("Authorization", "Bearer " + owner)
          .contentType("application/json")
          .content(json.writeValueAsString(body))
      )
      .andExpect(status().isOk());
    mvc
      .perform(
        delete("/api/activities/" + id).header(
          "Authorization",
          "Bearer " + owner
        )
      )
      .andExpect(status().isNoContent());
  }
}
