package com.tripboard.entity;
import jakarta.persistence.*;
import lombok.Getter;
@MappedSuperclass @Getter
public abstract class BaseEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) protected Long id;
}
