package com.tripboard.service;
import com.tripboard.entity.*;import com.tripboard.repository.*;import com.tripboard.security.CurrentUser;import com.tripboard.exception.ApiException;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor
public class AccessService {
 private final TripRepository trips;private final TripMemberRepository members;private final UserRepository users;private final CurrentUser current;
 // Serialize writes per trip, including membership changes, to keep financial references valid.
 public Trip member(Long id){var t=trips.findById(id).orElseThrow(ApiException::missing);check(t);return t;}
 public Trip write(Long id){var t=trips.lockById(id).orElseThrow(ApiException::missing);check(t);return t;}
 private void check(Trip t){if(!members.existsByTripIdAndUserId(t.getId(),current.id()))throw ApiException.forbidden();}
 public Trip owner(Long id){var t=write(id);if(!isOwner(t))throw ApiException.forbidden();return t;}
 public boolean isOwner(Trip t){return t.getOwner().getId().equals(current.id());}
 public void authorOrOwner(Trip t,User author){if(!isOwner(t)&&!author.getId().equals(current.id()))throw ApiException.forbidden();}
 public User user(){return users.findById(current.id()).orElseThrow(ApiException::missing);}
 public User participant(Long tripId,Long userId){return members.findByTripIdAndUserId(tripId,userId).orElseThrow(()->ApiException.bad("Select a current trip member.")).getUser();}
}
