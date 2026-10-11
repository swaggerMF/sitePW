package com.tripboard.service;
import com.tripboard.dto.SuggestionDtos.*;import com.tripboard.dto.ActivityDtos;import com.tripboard.entity.*;import com.tripboard.repository.*;import com.tripboard.security.CurrentUser;import com.tripboard.exception.ApiException;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class SuggestionService {
 private final SuggestionRepository suggestions;private final VoteRepository votes;private final AccessService access;private final CurrentUser current;private final ActivityService activities;
 public List<View> list(Long tripId){access.member(tripId);return suggestions.findByTripIdOrderByCreatedAtDescIdDesc(tripId).stream().map(this::view).toList();}
 public View get(Long id){var s=find(id);access.member(s.getTrip().getId());return view(s);}
 public View create(Long tripId,Input i){var t=access.write(tripId);var s=new Suggestion();s.setTrip(t);s.setCreatedBy(access.user());apply(s,i);return view(suggestions.save(s));}
 public View update(Long id,Input i){var s=write(id);access.authorOrOwner(s.getTrip(),s.getCreatedBy());open(s);apply(s,i);return view(s);}
 public void delete(Long id){var s=write(id);access.authorOrOwner(s.getTrip(),s.getCreatedBy());open(s);suggestions.delete(s);}
 public View vote(Long id,VoteInput i){var s=write(id);open(s);var v=votes.findBySuggestionIdAndUserId(id,current.id()).orElseGet(()->{var n=new Vote();n.setSuggestion(s);n.setUser(access.user());return n;});v.setValue(i.value());votes.saveAndFlush(v);return view(s);}
 public View decide(Long id,boolean accepted){var s=write(id);if(!access.isOwner(s.getTrip()))throw ApiException.forbidden();open(s);s.setStatus(accepted?"ACCEPTED":"REJECTED");return view(s);}
 public ActivityDtos.View convert(Long id,Convert i){var s=write(id);if(!access.isOwner(s.getTrip()))throw ApiException.forbidden();if(!s.getStatus().equals("ACCEPTED")||s.isConverted())throw ApiException.bad("Only accepted, unconverted ideas can be added to the itinerary.");var a=activities.createFor(s.getTrip(),s.getCreatedBy(),new ActivityDtos.Input(s.getTitle(),s.getDescription(),s.getLocation(),i.date(),i.startTime(),s.getEstimatedCost()));s.setActivity(a);s.setConverted(true);return activities.view(a);}
 private Suggestion find(Long id){return suggestions.findById(id).orElseThrow(ApiException::missing);}
 private Suggestion write(Long id){access.write(suggestions.tripId(id).orElseThrow(ApiException::missing));return find(id);}
 private void open(Suggestion s){if(!s.getStatus().equals("OPEN"))throw ApiException.bad("This idea has already been decided. Only open ideas can be changed or voted on.");}
 private void apply(Suggestion s,Input i){s.setTitle(i.title().trim());s.setDescription(i.description()==null?"":i.description().trim());s.setLocation(i.location()==null?"":i.location().trim());s.setEstimatedCost(i.estimatedCost());}
 private View view(Suggestion s){var all=votes.findBySuggestionId(s.getId());return new View(s.getId(),s.getTitle(),s.getDescription(),s.getLocation(),s.getEstimatedCost(),s.getStatus(),s.getCreatedBy().getId(),s.getCreatedBy().getUsername(),all.stream().filter(v->v.getValue().equals("UP")).count(),all.stream().filter(v->v.getValue().equals("DOWN")).count(),all.stream().filter(v->v.getUser().getId().equals(current.id())).map(Vote::getValue).findFirst().orElse(null),s.isConverted(),s.getActivity()==null?null:s.getActivity().getId(),s.getCreatedAt());}
}
