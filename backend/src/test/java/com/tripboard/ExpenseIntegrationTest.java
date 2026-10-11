package com.tripboard;
import org.junit.jupiter.api.Test;import org.springframework.boot.test.context.SpringBootTest;import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;import org.springframework.test.context.ActiveProfiles;import java.util.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class ExpenseIntegrationTest extends ApiTestSupport {
 @Test void expenseUpdateReplacesSharesAndProtectsFinancialMembers() throws Exception {
  String owner=register("expenseowner");register("expensemember");long trip=trip(owner);
  long member=postJson("/api/trips/"+trip+"/members",owner,Map.of("query","expensemember"),201).get("id").asLong();
  long ownerId=json.readTree(mvc.perform(get("/api/auth/me").header("Authorization","Bearer "+owner)).andReturn().getResponse().getContentAsString()).get("id").asLong();
  var body=new HashMap<String,Object>(Map.of("description","Dinner","amount","10.01","paidById",ownerId,"date","2027-07-13","participantIds",List.of(ownerId,member)));
  long id=postJson("/api/trips/"+trip+"/expenses",owner,body,201).get("id").asLong();
  mvc.perform(delete("/api/trips/"+trip+"/members/"+member).header("Authorization","Bearer "+owner)).andExpect(status().isBadRequest());
  body.put("amount","12.00");
  mvc.perform(put("/api/expenses/"+id).header("Authorization","Bearer "+owner).contentType("application/json").content(json.writeValueAsString(body))).andExpect(status().isOk()).andExpect(jsonPath("$.shares.length()").value(2)).andExpect(jsonPath("$.shares[0].amount").value(6.0));
  body.put("participantIds",List.of(ownerId,ownerId));postJson("/api/trips/"+trip+"/expenses",owner,body,400);
  mvc.perform(delete("/api/expenses/"+id).header("Authorization","Bearer "+owner)).andExpect(status().isNoContent());
  mvc.perform(delete("/api/trips/"+trip+"/members/"+member).header("Authorization","Bearer "+owner)).andExpect(status().isNoContent());
 }
}
