package com.github.lltal.observer.model;

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
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Entity
@Table(
        name = "tire_type_size",
        indexes = {
                @Index(name = "idx_type_tire_size", columnList = "tire_size", unique = true)
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class TireTypeSize {
    @Id
    @GeneratedValue(generator = DbConstants.GENERATOR_ID)
    private Long id;
    @Pattern(regexp = "^\\d+/(\\d+)/(\\d+)$")
    @Column(name = "tire_size", nullable = false, unique = true)
    private String tireSize;
}
