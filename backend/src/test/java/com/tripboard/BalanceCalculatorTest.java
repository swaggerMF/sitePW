package com.tripboard;
import com.tripboard.service.BalanceCalculator;import com.tripboard.service.BalanceCalculator.*;import org.junit.jupiter.api.Test;import java.math.BigDecimal;import java.util.*;import static org.assertj.core.api.Assertions.*;
class BalanceCalculatorTest {
 BalanceCalculator calc=new BalanceCalculator();BigDecimal n(String n){return new BigDecimal(n);}
 @Test void paidMinusShareProducesZeroSum(){var b=calc.balances(List.of(1L,2L,3L),List.of(new Amount(1L,n("600"))),List.of(new Amount(1L,n("200")),new Amount(2L,n("200")),new Amount(3L,n("200"))),List.of());assertThat(b.get(1L)).isEqualByComparingTo("400");assertThat(b.get(2L)).isEqualByComparingTo("-200");assertThat(b.values().stream().reduce(BigDecimal.ZERO,BigDecimal::add)).isZero();}
 @Test void settlementReducesDebtAndCredit(){var b=calc.balances(List.of(1L,2L),List.of(new Amount(1L,n("100"))),List.of(new Amount(1L,n("50")),new Amount(2L,n("50"))),List.of(new Payment(2L,1L,n("20"))));assertThat(b.get(1L)).isEqualByComparingTo("30");assertThat(b.get(2L)).isEqualByComparingTo("-30");}
 @Test void fullSettlementClearsBalances(){var b=calc.balances(List.of(1L,2L),List.of(new Amount(1L,n("100"))),List.of(new Amount(2L,n("100"))),List.of(new Payment(2L,1L,n("100"))));assertThat(calc.simplify(b)).isEmpty();}
 @Test void simplifiedTransfersClearAllMembers(){var b=new TreeMap<>(Map.of(1L,n("140"),2L,n("-80"),3L,n("-60")));var transfers=calc.simplify(b);assertThat(transfers).containsExactly(new Transfer(2L,1L,n("80")),new Transfer(3L,1L,n("60")));for(var t:transfers){b.put(t.fromUserId(),b.get(t.fromUserId()).add(t.amount()));b.put(t.toUserId(),b.get(t.toUserId()).subtract(t.amount()));}assertThat(b.values()).allMatch(v->v.signum()==0);}
 @Test void multipleCreditorsAndFractionalCents(){var b=Map.of(1L,n("2.01"),2L,n("1.00"),3L,n("-1.50"),4L,n("-1.51"));assertThat(calc.simplify(b).stream().map(Transfer::amount).reduce(BigDecimal.ZERO,BigDecimal::add)).isEqualByComparingTo("3.01");}
}
