package com.tripboard.controller;
import com.tripboard.dto.ActivityDtos.*;import com.tripboard.service.ActivityService;import jakarta.validation.Valid;import lombok.RequiredArgsConstructor;import org.springframework.web.bind.annotation.*;import org.springframework.http.HttpStatus;import java.util.List;
@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class ActivityController {
 private final ActivityService activities;
 @GetMapping("/trips/{tripId}/activities") public List<View> list(@PathVariable Long tripId){return activities.list(tripId);}
 @PostMapping("/trips/{tripId}/activities") @ResponseStatus(HttpStatus.CREATED) public View create(@PathVariable Long tripId,@Valid @RequestBody Input i){return activities.create(tripId,i);}
 @GetMapping("/activities/{id}") public View get(@PathVariable Long id){return activities.get(id);}
 @PutMapping("/activities/{id}") public View update(@PathVariable Long id,@Valid @RequestBody Input i){return activities.update(id,i);}
 @DeleteMapping("/activities/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){activities.delete(id);}
}
