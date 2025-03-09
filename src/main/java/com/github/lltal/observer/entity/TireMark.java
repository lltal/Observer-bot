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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(
        name = "tire_mark",
        indexes = {
                @Index(name = "idx_mark_name", columnList = "name")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class TireMark {

    @Id
    @GeneratedValue(generator = DbConstants.GENERATOR_ID)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "mark")
    private Collection<TireModel> models = new ArrayList<>();
}
