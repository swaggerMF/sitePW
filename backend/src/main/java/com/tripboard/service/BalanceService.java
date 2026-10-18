package com.tripboard.service;

import com.tripboard.dto.BalanceDtos.*;
import com.tripboard.repository.*;
import java.math.BigDecimal;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BalanceService {

  private final AccessService access;
  private final TripMemberRepository members;
  private final ExpenseRepository expenses;
  private final SettlementRepository settlements;
  private final BalanceCalculator calculator;

  public Summary summary(Long tripId) {
    access.member(tripId);
    var crew = members.findByTripIdOrderById(tripId);
    var costs = expenses.findByTripIdOrderByDateDescIdDesc(tripId);
    var payments = settlements.findByTripIdOrderByDateDescIdDesc(tripId);
    var paid = costs
      .stream()
      .map(e ->
        new BalanceCalculator.Amount(e.getPaidBy().getId(), e.getAmount())
      )
      .toList();
    var shares = costs
      .stream()
      .flatMap(e -> e.getShares().stream())
      .map(s ->
        new BalanceCalculator.Amount(s.getUser().getId(), s.getAmount())
      )
      .toList();
    var moves = payments
      .stream()
      .map(s ->
        new BalanceCalculator.Payment(
          s.getFromUser().getId(),
          s.getToUser().getId(),
          s.getAmount()
        )
      )
      .toList();
    var net = calculator.balances(
      crew
        .stream()
        .map(m -> m.getUser().getId())
        .toList(),
      paid,
      shares,
      moves
    );
    Map<Long, String> names = new HashMap<>();
    crew.forEach(m ->
      names.put(m.getUser().getId(), m.getUser().getUsername())
    );
    var balances = crew
      .stream()
      .map(m -> {
        Long id = m.getUser().getId();
        return new Balance(
          id,
          names.get(id),
          sum(paid, id),
          sum(shares, id),
          moves
            .stream()
            .filter(s -> s.fromUserId().equals(id))
            .map(BalanceCalculator.Payment::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add),
          moves
            .stream()
            .filter(s -> s.toUserId().equals(id))
            .map(BalanceCalculator.Payment::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add),
          net.get(id)
        );
      })
      .toList();
    var transfers = calculator
      .simplify(net)
      .stream()
      .map(t ->
        new Transfer(
          t.fromUserId(),
          names.get(t.fromUserId()),
          t.toUserId(),
          names.get(t.toUserId()),
          t.amount()
        )
      )
      .toList();
    return new Summary(
      costs
        .stream()
        .map(e -> e.getAmount())
        .reduce(BigDecimal.ZERO, BigDecimal::add),
      balances,
      transfers
    );
  }

  private BigDecimal sum(List<BalanceCalculator.Amount> entries, Long id) {
    return entries
      .stream()
      .filter(e -> e.userId().equals(id))
      .map(BalanceCalculator.Amount::amount)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
