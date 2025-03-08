package com.github.lltal.observer.output;

import com.github.lltal.observer.entity.TireTypeSize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TireTypeSizeRepo extends JpaRepository<TireTypeSize, Long> {
    TireTypeSize findByTireSize(String tireSize);

    void deleteByTireSize(String tireSize);
}
