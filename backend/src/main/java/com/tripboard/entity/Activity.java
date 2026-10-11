package com.tripboard.entity;
import jakarta.persistence.*;import lombok.*;import java.time.*;import java.math.BigDecimal;import org.hibernate.annotations.OnDelete;import org.hibernate.annotations.OnDeleteAction;
@Entity @Getter @Setter @NoArgsConstructor
public class Activity extends BaseEntity {
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @OnDelete(action=OnDeleteAction.CASCADE) private Trip trip;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private User createdBy;
 @Column(nullable=false,length=120) private String title;
 @Column(nullable=false,length=2000) private String description;
 @Column(nullable=false,length=200) private String location;
 @Column(nullable=false) private LocalDate date;
 @Column(nullable=false) private LocalTime startTime;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal estimatedCost;
}
