package com.tripboard.service;
import com.tripboard.entity.*;import com.tripboard.repository.*;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Component;import org.springframework.boot.CommandLineRunner;import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;import org.springframework.beans.factory.annotation.Value;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.transaction.annotation.Transactional;import java.time.*;import java.math.BigDecimal;import java.util.*;
@Component @RequiredArgsConstructor @ConditionalOnProperty(name="tripboard.demo",havingValue="true")
public class DemoData implements CommandLineRunner {
 private final UserRepository users;private final TripRepository trips;private final TripMemberRepository members;private final ActivityRepository activities;private final ExpenseRepository expenses;private final SuggestionRepository suggestions;private final VoteRepository votes;private final PasswordEncoder passwords;private final MoneyService money;
 @Value("${tripboard.demo-password}") private String password;
 @Override @Transactional public void run(String... args){
  if(users.count()>0)return;
  if(password.length()<10||password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72)throw new IllegalArgumentException("Set DEMO_PASSWORD to 10–72 UTF-8 bytes before enabling DEMO_DATA.");
  var vlad=user("Vlad");var andrei=user("Andrei");var mihai=user("Mihai");var crew=List.of(vlad,andrei,mihai);
  var t=trip("Iceland Road Trip","Iceland","Seven days of open roads, waterfalls and late-night skies. A shared adventure around the south coast, with plenty of time to take the scenic route.",LocalDate.of(2027,7,12),LocalDate.of(2027,7,18),vlad,crew);
  activity(t,vlad,"A slow morning in Reykjavik","Reykjavik",0,"10:00","0.00","Coffee, colorful streets and a walk along the harbor.");
  activity(t,andrei,"The Golden Circle","Thingvellir National Park",1,"09:00","15.00","Pack a lunch. We’ll stop at Geysir and Gullfoss along the way.");
  activity(t,vlad,"Chasing waterfalls at Skogafoss","Skogafoss",2,"09:00","0.00","Bring a waterproof layer and climb the stairs for the view.");
  activity(t,mihai,"Lunch in Vik","Vik",2,"12:00","25.00","A warm meal before the coast.");
  activity(t,mihai,"Black Sand Beach","Reynisfjara",2,"15:00","0.00","Watch the waves from a safe distance and follow local signs.");
  expense(t,vlad,"Rental car","600.00",crew);expense(t,andrei,"Hotel","420.00",crew);expense(t,mihai,"Fuel","95.00",crew);
  var lagoon=idea(t,mihai,"A soak in the Blue Lagoon","Grindavik","A warm-water reset after a few long days on the road. We should book a time slot together.","85.00");vote(lagoon,vlad,"UP");vote(lagoon,andrei,"UP");vote(lagoon,mihai,"UP");
  var hike=idea(t,andrei,"Sunset hike above Vik","Vik","A short walk with a big view. Let’s keep an eye on the weather.","0.00");vote(hike,vlad,"UP");vote(hike,mihai,"DOWN");
  idea(t,vlad,"Try a local food tour","Reykjavik","A few local favorites on our final evening in the city.","65.00");
  trip("A weekend in the Dolomites","Italy","Mountain trails, fresh air and a table for three. Let’s start collecting ideas.",LocalDate.of(2027,9,17),LocalDate.of(2027,9,20),andrei,crew);
 }
 private User user(String name){var u=new User();u.setUsername(name);u.setEmail(name.toLowerCase(Locale.ROOT)+"@tripboard.demo");u.setPasswordHash(passwords.encode(password));return users.save(u);}
 private Trip trip(String name,String destination,String description,LocalDate start,LocalDate end,User owner,List<User> crew){var t=new Trip();t.setName(name);t.setDestination(destination);t.setDescription(description);t.setStartDate(start);t.setEndDate(end);t.setCurrency("EUR");t.setOwner(owner);trips.save(t);for(var u:crew){var m=new TripMember();m.setTrip(t);m.setUser(u);m.setRole(u==owner?"OWNER":"MEMBER");members.save(m);}return t;}
 private void activity(Trip t,User u,String title,String location,int day,String time,String cost,String note){var a=new Activity();a.setTrip(t);a.setCreatedBy(u);a.setTitle(title);a.setLocation(location);a.setDescription(note);a.setDate(t.getStartDate().plusDays(day));a.setStartTime(LocalTime.parse(time));a.setEstimatedCost(new BigDecimal(cost));activities.save(a);}
 private void expense(Trip t,User u,String description,String amount,List<User> crew){var e=new Expense();e.setTrip(t);e.setCreatedBy(u);e.setPaidBy(u);e.setDescription(description);e.setAmount(new BigDecimal(amount));e.setDate(t.getStartDate());money.split(e.getAmount(),crew.stream().map(User::getId).toList()).forEach((id,share)->{var s=new ExpenseShare();s.setExpense(e);s.setUser(crew.stream().filter(m->m.getId().equals(id)).findFirst().orElseThrow());s.setAmount(share);e.getShares().add(s);});expenses.save(e);}
 private Suggestion idea(Trip t,User u,String title,String location,String description,String cost){var s=new Suggestion();s.setTrip(t);s.setCreatedBy(u);s.setTitle(title);s.setLocation(location);s.setDescription(description);s.setEstimatedCost(new BigDecimal(cost));return suggestions.save(s);}
 private void vote(Suggestion s,User u,String value){var v=new Vote();v.setSuggestion(s);v.setUser(u);v.setValue(value);votes.save(v);}
}
