package com.tripboard.repository;

import com.tripboard.entity.Suggestion;
import java.util.*;
import org.springframework.data.jpa.repository.*;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
  List<Suggestion> findByTripIdOrderByCreatedAtDescIdDesc(Long tripId);

  @Query("select s.trip.id from Suggestion s where s.id=:id")
  Optional<Long> tripId(Long id);
}
