package com.tripboard.repository;

import com.tripboard.entity.Expense;
import java.util.List;
import org.springframework.data.jpa.repository.*;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
  List<Expense> findByTripIdOrderByDateDescIdDesc(Long tripId);

  @Query(
    "select count(e)>0 from Expense e where e.trip.id=:tripId and (e.paidBy.id=:userId or exists(select s.id from ExpenseShare s where s.expense=e and s.user.id=:userId))"
  )
  boolean hasFinancialRecord(Long tripId, Long userId);
}
