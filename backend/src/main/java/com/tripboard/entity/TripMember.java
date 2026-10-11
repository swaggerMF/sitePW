package com.tripboard.entity;
import jakarta.persistence.*;import lombok.*;import org.hibernate.annotations.OnDelete;import org.hibernate.annotations.OnDeleteAction;
@Entity @Getter @Setter @NoArgsConstructor @Table(uniqueConstraints=@UniqueConstraint(columnNames={"trip_id","user_id"}))
public class TripMember extends BaseEntity {
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @OnDelete(action=OnDeleteAction.CASCADE) private Trip trip;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private User user;
 @Column(nullable=false,length=10) private String role;
}
