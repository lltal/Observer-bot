package com.github.lltal.observer.output;

import com.github.lltal.observer.model.Duty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;

public interface DutyRepo extends JpaRepository<Duty, Long> {
    @Query(value = "select * from duty where duty.createdAt >= :date", nativeQuery = true)
    Collection<Duty> findAllWhereCreatedAtBefore(@Param("date") Instant date);
}
