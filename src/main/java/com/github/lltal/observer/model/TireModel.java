package com.github.lltal.observer.model;

import com.github.lltal.observer.input.enumeration.Season;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(
        name = "tire_model",
        indexes = {
                @Index(name = "idx_mark_id", columnList = "mark_id"),
                @Index(
                        name = "idx_model_name", columnList = "name, season"
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class TireModel {

    @Id
    @GeneratedValue(generator = DbConstants.GENERATOR_ID)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, updatable = false)
    private int code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Season season;

    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "mark_id", referencedColumnName = "id")
    private TireMark mark;
}
