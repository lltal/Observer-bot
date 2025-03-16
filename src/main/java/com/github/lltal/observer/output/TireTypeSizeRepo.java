package com.github.lltal.observer.output;

import com.github.lltal.observer.model.TireTypeSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface TireTypeSizeRepo extends JpaRepository<TireTypeSize, Long> {

    TireTypeSize findByTireSize(String tireSize);

    void deleteByTireSize(String tireSize);

    @Query(value = "select tire_type_size.tire_size from tire_type_size", nativeQuery = true)
    Collection<String> findAllTireSize();

    boolean existsByTireSize(String tireSize);
}
