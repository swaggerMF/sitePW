package com.tripboard.repository;

import com.tripboard.entity.Trip;
import jakarta.persistence.LockModeType;
import java.util.*;
import org.springframework.data.jpa.repository.*;

public interface TripRepository extends JpaRepository<Trip, Long> {
  @Query(
    "select t from Trip t where t.id in (select m.trip.id from TripMember m where m.user.id=:userId) order by t.startDate desc"
  )
  List<Trip> forUser(Long userId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select t from Trip t where t.id=:id")
  Optional<Trip> lockById(Long id);
}
