package com.tripboard.service;
import com.tripboard.dto.ActivityDtos.*;import com.tripboard.entity.*;import com.tripboard.repository.ActivityRepository;import com.tripboard.exception.ApiException;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class ActivityService {
 private final ActivityRepository activities;private final AccessService access;
 public List<View> list(Long tripId){access.member(tripId);return activities.findByTripIdOrderByDateAscStartTimeAscIdAsc(tripId).stream().map(this::view).toList();}
 public View get(Long id){var a=find(id);access.member(a.getTrip().getId());return view(a);}
 public View create(Long tripId,Input i){var t=access.write(tripId);return view(createFor(t,access.user(),i));}
 public Activity createFor(Trip t,User author,Input i){validate(t,i);var a=new Activity();a.setTrip(t);a.setCreatedBy(author);apply(a,i);return activities.save(a);}
 public View update(Long id,Input i){var a=find(id);var t=access.write(a.getTrip().getId());access.authorOrOwner(t,a.getCreatedBy());validate(t,i);apply(a,i);return view(a);}
 public void delete(Long id){var a=find(id);var t=access.write(a.getTrip().getId());access.authorOrOwner(t,a.getCreatedBy());activities.delete(a);}
 private Activity find(Long id){return activities.findById(id).orElseThrow(ApiException::missing);}
 private void validate(Trip t,Input i){if(i.date().isBefore(t.getStartDate())||i.date().isAfter(t.getEndDate()))throw ApiException.bad("Activity date must be within the trip dates.");}
 private void apply(Activity a,Input i){a.setTitle(i.title().trim());a.setDescription(i.description()==null?"":i.description().trim());a.setLocation(i.location()==null?"":i.location().trim());a.setDate(i.date());a.setStartTime(i.startTime());a.setEstimatedCost(i.estimatedCost());}
 public View view(Activity a){return new View(a.getId(),a.getTitle(),a.getDescription(),a.getLocation(),a.getDate(),a.getStartTime(),a.getEstimatedCost(),a.getCreatedBy().getId(),a.getCreatedBy().getUsername());}
}
