package com.tripboard.service;
import com.tripboard.dto.DashboardView;import com.tripboard.security.CurrentUser;import com.tripboard.repository.SuggestionRepository;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.math.BigDecimal;import java.time.*;
@Service @RequiredArgsConstructor @Transactional
public class DashboardService {
 private final BalanceService balances;private final ActivityService activities;private final SuggestionRepository suggestions;private final CurrentUser current;
 public DashboardView get(Long tripId){var summary=balances.summary(tripId);var plan=activities.list(tripId);var now=LocalDateTime.now(ZoneOffset.UTC);return new DashboardView(summary.totalExpenses(),summary.balances().stream().filter(b->b.userId().equals(current.id())).map(b->b.balance()).findFirst().orElse(BigDecimal.ZERO),plan.size(),suggestions.findByTripIdOrderByCreatedAtDescIdDesc(tripId).stream().filter(s->s.getStatus().equals("OPEN")).count(),plan.stream().filter(a->!LocalDateTime.of(a.date(),a.startTime()).isBefore(now)).limit(3).toList());}
}
