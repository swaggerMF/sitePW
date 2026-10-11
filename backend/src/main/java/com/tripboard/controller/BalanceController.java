package com.tripboard.controller;
import com.tripboard.dto.BalanceDtos.*;import com.tripboard.service.*;import jakarta.validation.Valid;import lombok.RequiredArgsConstructor;import org.springframework.web.bind.annotation.*;import org.springframework.http.HttpStatus;import java.util.List;
@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class BalanceController {
 private final BalanceService balances;private final SettlementService settlements;
 @GetMapping("/trips/{tripId}/balances") public Summary balances(@PathVariable Long tripId){return balances.summary(tripId);}
 @GetMapping("/trips/{tripId}/settlements") public List<SettlementView> list(@PathVariable Long tripId){return settlements.list(tripId);}
 @PostMapping("/trips/{tripId}/settlements") @ResponseStatus(HttpStatus.CREATED) public SettlementView create(@PathVariable Long tripId,@Valid @RequestBody Input i){return settlements.create(tripId,i);}
 @DeleteMapping("/settlements/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){settlements.delete(id);}
}
