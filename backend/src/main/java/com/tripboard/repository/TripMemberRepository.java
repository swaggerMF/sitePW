package com.tripboard.repository;
import com.tripboard.entity.TripMember;import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface TripMemberRepository extends JpaRepository<TripMember,Long> {
 boolean existsByTripIdAndUserId(Long tripId,Long userId);
 Optional<TripMember> findByTripIdAndUserId(Long tripId,Long userId);
 List<TripMember> findByTripIdOrderById(Long tripId);
 long countByTripId(Long tripId);
}
