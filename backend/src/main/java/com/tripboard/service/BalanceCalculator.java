package com.tripboard.service;
import org.springframework.stereotype.Service;import java.math.BigDecimal;import java.util.*;
@Service
public class BalanceCalculator {
 public record Amount(Long userId,BigDecimal amount){}
 public record Payment(Long fromUserId,Long toUserId,BigDecimal amount){}
 public record Transfer(Long fromUserId,Long toUserId,BigDecimal amount){}
 /** Positive means receivable. Sending a settlement reduces debt; receiving reduces credit. */
 public Map<Long,BigDecimal> balances(List<Long> members,List<Amount> paid,List<Amount> shares,List<Payment> settlements){
  Map<Long,BigDecimal> net=new TreeMap<>();members.forEach(id->net.put(id,new BigDecimal("0.00")));
  paid.forEach(p->net.merge(p.userId(),p.amount(),BigDecimal::add));shares.forEach(s->net.merge(s.userId(),s.amount().negate(),BigDecimal::add));
  settlements.forEach(s->{net.merge(s.fromUserId(),s.amount(),BigDecimal::add);net.merge(s.toUserId(),s.amount().negate(),BigDecimal::add);});return net;
 }
 /** Greedily match largest debtor and creditor; ties are stable by user ID. */
 public List<Transfer> simplify(Map<Long,BigDecimal> balances){
  if(balances.values().stream().reduce(BigDecimal.ZERO,BigDecimal::add).signum()!=0)throw new IllegalArgumentException("Balances must sum to zero");
  var remaining=new TreeMap<>(balances);var result=new ArrayList<Transfer>();
  while(true){Long debtor=null,creditor=null;for(var e:remaining.entrySet()){if(e.getValue().signum()<0&&(debtor==null||e.getValue().compareTo(remaining.get(debtor))<0))debtor=e.getKey();if(e.getValue().signum()>0&&(creditor==null||e.getValue().compareTo(remaining.get(creditor))>0))creditor=e.getKey();}
   if(debtor==null||creditor==null)break;BigDecimal amount=remaining.get(debtor).negate().min(remaining.get(creditor));result.add(new Transfer(debtor,creditor,amount));remaining.put(debtor,remaining.get(debtor).add(amount));remaining.put(creditor,remaining.get(creditor).subtract(amount));
  }return result;
 }
}
