package com.github.lltal.observer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(
        name = "location",
        indexes = {
                @Index(name = "idx_street", columnList = "street", unique = true)
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(generator = DbConstants.GENERATOR_ID)
    private Long id;

    @Column(unique = true, nullable = false)
    private String street;
}
