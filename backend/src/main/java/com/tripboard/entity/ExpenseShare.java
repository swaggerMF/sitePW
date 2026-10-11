package com.tripboard.entity;
import jakarta.persistence.*;import lombok.*;import java.math.BigDecimal;import org.hibernate.annotations.OnDelete;import org.hibernate.annotations.OnDeleteAction;
@Entity @Getter @Setter @NoArgsConstructor @Table(uniqueConstraints=@UniqueConstraint(columnNames={"expense_id","user_id"}))
public class ExpenseShare extends BaseEntity {
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @OnDelete(action=OnDeleteAction.CASCADE) private Expense expense;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private User user;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal amount;
}
