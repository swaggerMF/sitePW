package com.tripboard.dto;
import java.math.BigDecimal;import java.time.LocalDate;import java.util.List;import jakarta.validation.constraints.*;
public final class BalanceDtos {
 public record Balance(Long userId,String username,BigDecimal totalPaid,BigDecimal totalShare,BigDecimal settlementsSent,BigDecimal settlementsReceived,BigDecimal balance){}
 public record Transfer(Long fromUserId,String fromUser,Long toUserId,String toUser,BigDecimal amount){}
 public record Summary(BigDecimal totalExpenses,List<Balance> balances,List<Transfer> transfers){}
 public record Input(@NotNull Long fromUserId,@NotNull Long toUserId,@NotNull @DecimalMin("0.01") @Digits(integer=10,fraction=2) BigDecimal amount,@NotNull LocalDate date){}
 public record SettlementView(Long id,Long fromUserId,String fromUser,Long toUserId,String toUser,BigDecimal amount,LocalDate date,Long createdById){}
}
