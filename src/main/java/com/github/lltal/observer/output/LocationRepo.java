package com.github.lltal.observer.output;

import com.github.lltal.observer.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<Location, Long> {
    Location findByStreet(String street);

    void deleteByStreet(String street);
}
