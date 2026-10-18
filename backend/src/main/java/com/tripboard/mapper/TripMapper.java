package com.tripboard.mapper;

import com.tripboard.dto.TripDtos.*;
import com.tripboard.entity.*;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {

  public View view(Trip t, long count) {
    return new View(
      t.getId(),
      t.getName(),
      t.getDestination(),
      t.getDescription(),
      t.getStartDate(),
      t.getEndDate(),
      t.getCurrency(),
      t.getOwner().getId(),
      count,
      t.getCreatedAt()
    );
  }

  public Member member(TripMember m) {
    return new Member(
      m.getUser().getId(),
      m.getUser().getUsername(),
      m.getUser().getEmail(),
      m.getRole()
    );
  }

  public void apply(Trip t, Input i) {
    t.setName(i.name().trim());
    t.setDestination(i.destination().trim());
    t.setDescription(i.description() == null ? "" : i.description().trim());
    t.setStartDate(i.startDate());
    t.setEndDate(i.endDate());
    t.setCurrency(i.currency());
  }
}
