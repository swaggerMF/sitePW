package com.tripboard.service;

import com.tripboard.dto.ExpenseDtos.*;
import com.tripboard.entity.*;
import com.tripboard.exception.ApiException;
import com.tripboard.repository.ExpenseRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

  private final ExpenseRepository expenses;
  private final AccessService access;
  private final MoneyService money;

  public List<View> list(Long tripId) {
    access.member(tripId);
    return expenses
      .findByTripIdOrderByDateDescIdDesc(tripId)
      .stream()
      .map(this::view)
      .toList();
  }

  public View get(Long id) {
    var e = find(id);
    access.member(e.getTrip().getId());
    return view(e);
  }

  public View create(Long tripId, Input i) {
    var t = access.write(tripId);
    var e = new Expense();
    e.setTrip(t);
    e.setCreatedBy(access.user());
    apply(e, i);
    return view(expenses.save(e));
  }

  public View update(Long id, Input i) {
    var e = find(id);
    var t = access.write(e.getTrip().getId());
    access.authorOrOwner(t, e.getCreatedBy());
    e.getShares().clear();
    expenses.flush();
    apply(e, i);
    return view(e);
  }

  public void delete(Long id) {
    var e = find(id);
    var t = access.write(e.getTrip().getId());
    access.authorOrOwner(t, e.getCreatedBy());
    expenses.delete(e);
  }

  private Expense find(Long id) {
    return expenses.findById(id).orElseThrow(ApiException::missing);
  }

  private void apply(Expense e, Input i) {
    e.setDescription(i.description().trim());
    e.setAmount(i.amount());
    e.setDate(i.date());
    e.setPaidBy(access.participant(e.getTrip().getId(), i.paidById()));
    money
      .split(i.amount(), i.participantIds())
      .forEach((id, amount) -> {
        var s = new ExpenseShare();
        s.setExpense(e);
        s.setUser(access.participant(e.getTrip().getId(), id));
        s.setAmount(amount);
        e.getShares().add(s);
      });
  }

  private View view(Expense e) {
    return new View(
      e.getId(),
      e.getDescription(),
      e.getAmount(),
      e.getPaidBy().getId(),
      e.getPaidBy().getUsername(),
      e.getDate(),
      e.getCreatedBy().getId(),
      e
        .getShares()
        .stream()
        .map(s ->
          new Share(
            s.getUser().getId(),
            s.getUser().getUsername(),
            s.getAmount()
          )
        )
        .toList()
    );
  }
}
