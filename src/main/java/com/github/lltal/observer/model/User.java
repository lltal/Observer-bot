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

@Getter
@Setter
@Entity
@Table(
        name = "usr",
        indexes = {
                @Index(name = "idx_usr_tg_id", columnList = "tg_id", unique = true)
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = DbConstants.GENERATOR_ID)
    private Long id;

    @Column(name = "tg_id", unique = true, nullable = false)
    private String tgId;
}
