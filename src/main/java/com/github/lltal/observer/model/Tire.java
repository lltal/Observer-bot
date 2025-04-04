package com.github.lltal.observer.model;

import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(
        name = "tire",
        indexes = {
                @Index(name = "idx_duty_id", columnList = "duty_id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class Tire {
    @Id
    @GeneratedValue(generator = DbConstants.GENERATOR_ID)
    private Long id;
    @Column(columnDefinition = "TIMESTAMPTZ", nullable = false)
    private Instant createdAt;
    @Pattern(regexp = UiHelper.CAR_NUMBER_PATTERN)
    @Column(nullable = false)
    private String carNumber;
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "tire_model_id", referencedColumnName = "id")
    private TireModel tireModel;
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "type_size_id", referencedColumnName = "id")
    private TireTypeSize typeSize;
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "duty_id", referencedColumnName = "id")
    private Duty duty;
}
