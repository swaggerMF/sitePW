package com.tripboard.service;
import com.tripboard.dto.BalanceDtos.*;import com.tripboard.entity.Settlement;import com.tripboard.repository.SettlementRepository;import com.tripboard.exception.ApiException;import lombok.RequiredArgsConstructor;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class SettlementService {
 private final SettlementRepository settlements;private final AccessService access;
 public List<SettlementView> list(Long tripId){access.member(tripId);return settlements.findByTripIdOrderByDateDescIdDesc(tripId).stream().map(this::view).toList();}
 public SettlementView create(Long tripId,Input i){var t=access.write(tripId);if(i.fromUserId().equals(i.toUserId()))throw ApiException.bad("Choose two different members.");var user=access.user();if(!access.isOwner(t)&&!user.getId().equals(i.fromUserId())&&!user.getId().equals(i.toUserId()))throw ApiException.forbidden();var s=new Settlement();s.setTrip(t);s.setCreatedBy(user);s.setFromUser(access.participant(tripId,i.fromUserId()));s.setToUser(access.participant(tripId,i.toUserId()));s.setAmount(i.amount());s.setDate(i.date());return view(settlements.save(s));}
 public void delete(Long id){var s=settlements.findById(id).orElseThrow(ApiException::missing);var t=access.write(s.getTrip().getId());access.authorOrOwner(t,s.getCreatedBy());settlements.delete(s);}
 private SettlementView view(Settlement s){return new SettlementView(s.getId(),s.getFromUser().getId(),s.getFromUser().getUsername(),s.getToUser().getId(),s.getToUser().getUsername(),s.getAmount(),s.getDate(),s.getCreatedBy().getId());}
}
