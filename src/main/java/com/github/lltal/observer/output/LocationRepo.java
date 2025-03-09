package com.github.lltal.observer.output;

import com.github.lltal.observer.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface LocationRepo extends JpaRepository<Location, Long> {
    Location findByStreet(String street);

    void deleteByStreet(String street);

    @Query(value = "select location.street from location", nativeQuery = true)
    Collection<String> findAllStreet();

    boolean existsByStreet(String street);
}
