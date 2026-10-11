package com.tripboard;
import com.fasterxml.jackson.databind.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.*;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
abstract class ApiTestSupport {
 @Autowired protected MockMvc mvc;@Autowired protected ObjectMapper json;
 protected JsonNode postJson(String path,String token,Object body,int status) throws Exception {return json.readTree(mvc.perform(post(path).header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(body))).andExpect(status().is(status)).andReturn().getResponse().getContentAsString());}
 protected String register(String name) throws Exception {return json.readTree(mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(java.util.Map.of("username",name,"email",name+"@example.com","password","test-password-123")))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString()).get("token").asText();}
 protected long trip(String token) throws Exception {return postJson("/api/trips",token,java.util.Map.of("name","Test trip","destination","Iceland","startDate","2027-07-12","endDate","2027-07-18","currency","EUR"),201).get("id").asLong();}
}
