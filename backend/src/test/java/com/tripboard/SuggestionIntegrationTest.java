package com.tripboard;
import org.junit.jupiter.api.Test;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.test.context.SpringBootTest;import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;import org.springframework.test.context.ActiveProfiles;import com.tripboard.repository.VoteRepository;import java.util.Map;import static org.assertj.core.api.Assertions.*;import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class SuggestionIntegrationTest extends ApiTestSupport {
 @Autowired VoteRepository votes;
 @Test void uniqueVotesCanChangeAndAcceptedIdeaConvertsOnlyOnce() throws Exception {
  String owner=register("ideaowner"),member=register("ideamember");long trip=trip(owner);postJson("/api/trips/"+trip+"/members",owner,Map.of("query","ideamember"),201);
  long id=postJson("/api/trips/"+trip+"/suggestions",member,Map.of("title","Blue Lagoon","estimatedCost","85.00"),201).get("id").asLong();
  postJson("/api/suggestions/"+id+"/vote",member,Map.of("value","UP"),200);postJson("/api/suggestions/"+id+"/vote",member,Map.of("value","UP"),200);
  var changed=postJson("/api/suggestions/"+id+"/vote",member,Map.of("value","DOWN"),200);assertThat(votes.countBySuggestionId(id)).isEqualTo(1);assertThat(changed.get("upVotes").asInt()).isZero();assertThat(changed.get("downVotes").asInt()).isEqualTo(1);assertThat(changed.get("myVote").asText()).isEqualTo("DOWN");
  postJson("/api/suggestions/"+id+"/accept",member,Map.of(),403);postJson("/api/suggestions/"+id+"/accept",owner,Map.of(),200);
  postJson("/api/suggestions/"+id+"/vote",member,Map.of("value","UP"),400);
  postJson("/api/suggestions/"+id+"/convert",owner,Map.of("date","2027-07-14","startTime","10:00"),201);
  postJson("/api/suggestions/"+id+"/convert",owner,Map.of("date","2027-07-14","startTime","10:00"),400);
  mvc.perform(delete("/api/suggestions/"+id).header("Authorization","Bearer "+owner)).andExpect(status().isBadRequest());
 }
}
