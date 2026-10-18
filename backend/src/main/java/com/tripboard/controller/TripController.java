package com.tripboard.controller;

import com.tripboard.dto.TripDtos.*;
import com.tripboard.service.*;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

  private final TripService trips;
  private final MemberService members;

  @GetMapping
  public List<View> list() {
    return trips.list();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public View create(@Valid @RequestBody Input i) {
    return trips.create(i);
  }

  @GetMapping("/{id}")
  public View get(@PathVariable Long id) {
    return trips.get(id);
  }

  @PutMapping("/{id}")
  public View update(@PathVariable Long id, @Valid @RequestBody Input i) {
    return trips.update(id, i);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    trips.delete(id);
  }

  @GetMapping("/{id}/members")
  public List<Member> members(@PathVariable Long id) {
    return members.list(id);
  }

  @PostMapping("/{id}/members")
  @ResponseStatus(HttpStatus.CREATED)
  public Member add(@PathVariable Long id, @Valid @RequestBody AddMember i) {
    return members.add(id, i);
  }

  @DeleteMapping("/{id}/members/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remove(@PathVariable Long id, @PathVariable Long userId) {
    members.remove(id, userId);
  }
}
