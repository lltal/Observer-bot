package com.github.lltal.observer.service.back.base.internal;

import com.github.lltal.observer.input.dto.LocationDto;
import com.github.lltal.observer.model.Location;
import com.github.lltal.observer.output.LocationRepo;
import com.github.lltal.observer.service.back.base.PrivateBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class LocationPrivateBackService implements PrivateBackService {
    private final LocationRepo repo;

    public void save(LocationDto dto) {
        Location location = new Location();
        location.setStreet(dto.getStreet());
        repo.save(location);
    }

    public Collection<String> findAllStreets() {
        return repo.findAllStreet();
    }

    @Transactional
    public void delete(String street) {
        repo.deleteByStreet(street);
    }

    public boolean contains(String street) {
        return repo.existsByStreet(street);
    }
}
