package com.tripboard.service;

import com.tripboard.exception.ApiException;
import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class MoneyService {

  /** Remainder cents go to ascending user IDs, independently of checkbox order. */
  public Map<Long, BigDecimal> split(
    BigDecimal amount,
    List<Long> participants
  ) {
    if (
      amount == null ||
      amount.signum() <= 0 ||
      participants == null ||
      participants.isEmpty()
    ) throw ApiException.bad(
      "A positive amount and participants are required."
    );
    var ids = participants.stream().distinct().sorted().toList();
    if (ids.size() != participants.size()) throw ApiException.bad(
      "Each participant must appear only once."
    );
    long cents;
    try {
      cents = amount.movePointRight(2).longValueExact();
    } catch (ArithmeticException e) {
      throw ApiException.bad("Use at most two decimal places.");
    }
    long each = cents / ids.size(),
      remainder = cents % ids.size();
    var result = new LinkedHashMap<Long, BigDecimal>();
    for (int i = 0; i < ids.size(); i++) result.put(
      ids.get(i),
      BigDecimal.valueOf(each + (i < remainder ? 1 : 0), 2)
    );
    return result;
  }
}
