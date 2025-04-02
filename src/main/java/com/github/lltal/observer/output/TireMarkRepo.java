package com.github.lltal.observer.output;

import com.github.lltal.observer.model.TireMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface TireMarkRepo extends JpaRepository<TireMark, Long> {
    TireMark findByName(String name);

    void deleteByName(String name);

    @Query(value = "select tire_mark.name from tire_mark", nativeQuery = true)
    Collection<String> findAllName();

    @Query(value = "select trma.name from tire_mark trma" +
            " join tire_model trmo on trmo.mark_id = trma.id", nativeQuery = true)
    Collection<String> findAllNameWithoutEmptyModels();

    boolean existsByName(String name);
}
