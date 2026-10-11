package com.tripboard.controller;
import com.tripboard.dto.SuggestionDtos.*;import com.tripboard.dto.ActivityDtos;import com.tripboard.service.SuggestionService;import jakarta.validation.Valid;import lombok.RequiredArgsConstructor;import org.springframework.web.bind.annotation.*;import org.springframework.http.HttpStatus;import java.util.List;
@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class SuggestionController {
 private final SuggestionService suggestions;
 @GetMapping("/trips/{tripId}/suggestions") public List<View> list(@PathVariable Long tripId){return suggestions.list(tripId);}
 @PostMapping("/trips/{tripId}/suggestions") @ResponseStatus(HttpStatus.CREATED) public View create(@PathVariable Long tripId,@Valid @RequestBody Input i){return suggestions.create(tripId,i);}
 @GetMapping("/suggestions/{id}") public View get(@PathVariable Long id){return suggestions.get(id);}
 @PutMapping("/suggestions/{id}") public View update(@PathVariable Long id,@Valid @RequestBody Input i){return suggestions.update(id,i);}
 @DeleteMapping("/suggestions/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){suggestions.delete(id);}
 @PostMapping("/suggestions/{id}/vote") public View vote(@PathVariable Long id,@Valid @RequestBody VoteInput i){return suggestions.vote(id,i);}
 @PostMapping("/suggestions/{id}/accept") public View accept(@PathVariable Long id){return suggestions.decide(id,true);}
 @PostMapping("/suggestions/{id}/reject") public View reject(@PathVariable Long id){return suggestions.decide(id,false);}
 @PostMapping("/suggestions/{id}/convert") @ResponseStatus(HttpStatus.CREATED) public ActivityDtos.View convert(@PathVariable Long id,@Valid @RequestBody Convert i){return suggestions.convert(id,i);}
}
