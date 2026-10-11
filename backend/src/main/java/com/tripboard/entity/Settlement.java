package com.tripboard.entity;
import jakarta.persistence.*;import lombok.*;import java.time.LocalDate;import java.math.BigDecimal;import org.hibernate.annotations.OnDelete;import org.hibernate.annotations.OnDeleteAction;
@Entity @Getter @Setter @NoArgsConstructor
public class Settlement extends BaseEntity {
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @OnDelete(action=OnDeleteAction.CASCADE) private Trip trip;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private User fromUser;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private User toUser;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private User createdBy;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal amount;
 @Column(nullable=false) private LocalDate date;
}
