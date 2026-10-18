package com.tripboard.repository;

import com.tripboard.entity.Vote;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
  Optional<Vote> findBySuggestionIdAndUserId(Long suggestionId, Long userId);
  List<Vote> findBySuggestionId(Long suggestionId);
  long countBySuggestionId(Long suggestionId);
}
