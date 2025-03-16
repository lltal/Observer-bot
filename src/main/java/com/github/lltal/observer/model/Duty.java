package com.github.lltal.observer.model;

import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(
        name = "duty"
)
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class Duty {
    @Id
    @GeneratedValue(generator = DbConstants.GENERATOR_ID)
    private Long id;
    @Column(columnDefinition = "TIMESTAMPTZ", nullable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private String fio;
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = false
    )
    @JoinColumn(name = "user_size_id", referencedColumnName = "id")
    private User user;
    @Pattern(regexp = UiHelper.PHONE_NUMBER_PATTERN)
    @Column(updatable = false)
    private String phoneNumber;
    @OneToMany(mappedBy = "duty", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<Tire> tires = new ArrayList<>();
}
