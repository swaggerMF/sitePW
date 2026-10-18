package com.tripboard.repository;

import com.tripboard.entity.Activity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
  List<Activity> findByTripIdOrderByDateAscStartTimeAscIdAsc(Long tripId);
  boolean existsByTripIdAndDateBefore(Long tripId, LocalDate date);
  boolean existsByTripIdAndDateAfter(Long tripId, LocalDate date);
}
