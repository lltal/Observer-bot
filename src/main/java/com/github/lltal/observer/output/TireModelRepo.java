package com.github.lltal.observer.output;

import com.github.lltal.observer.entity.TireMark;
import com.github.lltal.observer.entity.TireModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TireModelRepo extends JpaRepository<TireModel, Long> {
    TireModel findByName(String name);

    void deleteByNameAndMark(String name, TireMark mark);

    void deleteByName(String name);
}
