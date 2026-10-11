package com.tripboard.service;
import com.tripboard.entity.*;import com.tripboard.dto.TripDtos.*;import com.tripboard.repository.*;import com.tripboard.mapper.TripMapper;import com.tripboard.security.CurrentUser;import com.tripboard.exception.ApiException;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class TripService {
 private final TripRepository trips;private final TripMemberRepository members;private final TripMapper mapper;private final AccessService access;private final CurrentUser current;
 public List<View> list(){return trips.forUser(current.id()).stream().map(this::view).toList();}
 public View get(Long id){return view(access.member(id));}
 public View create(Input i){validate(i);var t=new Trip();mapper.apply(t,i);t.setOwner(access.user());trips.save(t);var m=new TripMember();m.setTrip(t);m.setUser(t.getOwner());m.setRole("OWNER");members.save(m);return view(t);}
 public View update(Long id,Input i){validate(i);var t=access.owner(id);if(!t.getCurrency().equals(i.currency()))throw ApiException.bad("Trip currency cannot be changed. Create a separate trip for another currency.");mapper.apply(t,i);return view(t);}
 public void delete(Long id){trips.delete(access.owner(id));}
 private void validate(Input i){if(i.endDate().isBefore(i.startDate()))throw ApiException.bad("End date must be on or after start date.");}
 private View view(Trip t){return mapper.view(t,members.countByTripId(t.getId()));}
}
