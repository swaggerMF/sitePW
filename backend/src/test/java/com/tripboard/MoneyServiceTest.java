package com.tripboard;
import com.tripboard.service.MoneyService;import com.tripboard.exception.ApiException;import org.junit.jupiter.api.Test;import java.math.BigDecimal;import java.util.List;import static org.assertj.core.api.Assertions.*;
class MoneyServiceTest {
 MoneyService money=new MoneyService();
 @Test void splitsEvenly(){assertThat(money.split(new BigDecimal("600.00"),List.of(1L,2L,3L)).values()).containsOnly(new BigDecimal("200.00"));}
 @Test void preservesRemainderDeterministically(){var shares=money.split(new BigDecimal("10.00"),List.of(3L,1L,2L));assertThat(shares.get(1L)).isEqualByComparingTo("3.34");assertThat(shares.values().stream().reduce(BigDecimal.ZERO,BigDecimal::add)).isEqualByComparingTo("10.00");assertThat(shares).isEqualTo(money.split(new BigDecimal("10.00"),List.of(2L,3L,1L)));}
 @Test void canSplitOneCent(){var shares=money.split(new BigDecimal("0.01"),List.of(1L,2L,3L));assertThat(shares.get(1L)).isEqualByComparingTo("0.01");assertThat(shares.get(2L)).isEqualByComparingTo("0.00");}
 @Test void rejectsInvalidSplits(){assertThatThrownBy(()->money.split(BigDecimal.ONE,List.of())).isInstanceOf(ApiException.class);assertThatThrownBy(()->money.split(BigDecimal.ONE,List.of(1L,1L))).isInstanceOf(ApiException.class);assertThatThrownBy(()->money.split(new BigDecimal("1.001"),List.of(1L))).isInstanceOf(ApiException.class);}
}
