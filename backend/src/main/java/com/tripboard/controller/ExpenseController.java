package com.tripboard.controller;
import com.tripboard.dto.ExpenseDtos.*;import com.tripboard.service.ExpenseService;import jakarta.validation.Valid;import lombok.RequiredArgsConstructor;import org.springframework.web.bind.annotation.*;import org.springframework.http.HttpStatus;import java.util.List;
@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class ExpenseController {
 private final ExpenseService expenses;
 @GetMapping("/trips/{tripId}/expenses") public List<View> list(@PathVariable Long tripId){return expenses.list(tripId);}
 @PostMapping("/trips/{tripId}/expenses") @ResponseStatus(HttpStatus.CREATED) public View create(@PathVariable Long tripId,@Valid @RequestBody Input i){return expenses.create(tripId,i);}
 @GetMapping("/expenses/{id}") public View get(@PathVariable Long id){return expenses.get(id);}
 @PutMapping("/expenses/{id}") public View update(@PathVariable Long id,@Valid @RequestBody Input i){return expenses.update(id,i);}
 @DeleteMapping("/expenses/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){expenses.delete(id);}
}
