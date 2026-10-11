package com.tripboard.repository;
import com.tripboard.entity.Vote;import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface VoteRepository extends JpaRepository<Vote,Long> {
 Optional<Vote> findBySuggestionIdAndUserId(Long suggestionId,Long userId);
 List<Vote> findBySuggestionId(Long suggestionId);
 long countBySuggestionId(Long suggestionId);
}
