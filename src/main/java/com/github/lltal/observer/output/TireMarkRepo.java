package com.github.lltal.observer.output;

import com.github.lltal.observer.entity.TireMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TireMarkRepo extends JpaRepository<TireMark, Long> {
    TireMark findByName(String name);

    void deleteByName(String name);

    Collection<String> findAllName();
}
