package com.github.lltal.observer.output;

import com.github.lltal.observer.input.dto.DutyDto;
import com.github.lltal.observer.model.Duty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;

public interface DutyRepo extends JpaRepository<Duty, Long> {

    @Query(value = "select * from duty where duty.created_at between :startDate and :endDate", nativeQuery = true)
    Collection<Duty> findAllByDate(Instant startDate, Instant endDate);
}
