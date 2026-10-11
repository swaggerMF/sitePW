package com.tripboard.repository;
import com.tripboard.entity.Suggestion;import org.springframework.data.jpa.repository.*;import java.util.*;
public interface SuggestionRepository extends JpaRepository<Suggestion,Long> {
 List<Suggestion> findByTripIdOrderByCreatedAtDescIdDesc(Long tripId);
 @Query("select s.trip.id from Suggestion s where s.id=:id") Optional<Long> tripId(Long id);
}
