package com.tripboard;
import org.junit.jupiter.api.Test;import org.springframework.boot.test.context.SpringBootTest;import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;import org.springframework.test.context.ActiveProfiles;import java.util.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class SettlementIntegrationTest extends ApiTestSupport {
 @Test void persistedSettlementReducesDebtAndCanBeReversed() throws Exception {
  String owner=register("settleowner"),memberToken=register("settlemember"),third=register("settlethird");long trip=trip(owner);
  long member=postJson("/api/trips/"+trip+"/members",owner,Map.of("query","settlemember"),201).get("id").asLong();
  postJson("/api/trips/"+trip+"/members",owner,Map.of("query","settlethird"),201);
  long ownerId=json.readTree(mvc.perform(get("/api/auth/me").header("Authorization","Bearer "+owner)).andReturn().getResponse().getContentAsString()).get("id").asLong();
  postJson("/api/trips/"+trip+"/expenses",owner,Map.of("description","Car","amount","100","paidById",ownerId,"date","2027-07-13","participantIds",List.of(ownerId,member)),201);
  var payment=Map.of("fromUserId",member,"toUserId",ownerId,"amount","50.00","date","2027-07-14");
  postJson("/api/trips/"+trip+"/settlements",third,payment,403);
  long id=postJson("/api/trips/"+trip+"/settlements",memberToken,payment,201).get("id").asLong();
  mvc.perform(get("/api/trips/"+trip+"/balances").header("Authorization","Bearer "+owner)).andExpect(status().isOk()).andExpect(jsonPath("$.transfers.length()").value(0));
  mvc.perform(delete("/api/settlements/"+id).header("Authorization","Bearer "+owner)).andExpect(status().isNoContent());
  mvc.perform(get("/api/trips/"+trip+"/balances").header("Authorization","Bearer "+owner)).andExpect(status().isOk()).andExpect(jsonPath("$.transfers[0].amount").value(50.0));
 }
}
