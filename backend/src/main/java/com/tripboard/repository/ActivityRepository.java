package com.tripboard.repository;
import com.tripboard.entity.Activity;import org.springframework.data.jpa.repository.JpaRepository;import java.util.List;import java.time.LocalDate;
public interface ActivityRepository extends JpaRepository<Activity,Long> {
 List<Activity> findByTripIdOrderByDateAscStartTimeAscIdAsc(Long tripId);
 boolean existsByTripIdAndDateBefore(Long tripId,LocalDate date);
 boolean existsByTripIdAndDateAfter(Long tripId,LocalDate date);
}
