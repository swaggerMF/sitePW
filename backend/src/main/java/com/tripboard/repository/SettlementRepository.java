package com.tripboard.repository;
import com.tripboard.entity.Settlement;import org.springframework.data.jpa.repository.*;import java.util.List;
public interface SettlementRepository extends JpaRepository<Settlement,Long> {
 List<Settlement> findByTripIdOrderByDateDescIdDesc(Long tripId);
 @Query("select count(s)>0 from Settlement s where s.trip.id=:tripId and (s.fromUser.id=:userId or s.toUser.id=:userId)") boolean hasFinancialRecord(Long tripId,Long userId);
}
