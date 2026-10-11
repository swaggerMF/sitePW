package com.tripboard.entity;
import jakarta.persistence.*;import lombok.*;import org.hibernate.annotations.OnDelete;import org.hibernate.annotations.OnDeleteAction;
@Entity @Getter @Setter @NoArgsConstructor @Table(uniqueConstraints=@UniqueConstraint(columnNames={"suggestion_id","user_id"}))
public class Vote extends BaseEntity {
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @OnDelete(action=OnDeleteAction.CASCADE) private Suggestion suggestion;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private User user;
 @Column(name="vote_value",nullable=false,length=4) private String value;
}
