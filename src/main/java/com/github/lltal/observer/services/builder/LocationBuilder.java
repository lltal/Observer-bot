package com.github.lltal.observer.services.builder;

import com.github.lltal.observer.entity.Location;
import com.github.lltal.observer.input.dto.LocationDto;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LocationBuilder {

    public Location buildModel(LocationDto dto) {
        return Location.builder()
                .street(dto.getStreet())
                .build();
    }

    public LocationDto buildDto(Location model) {
        return LocationDto.builder()
                .street(model.getStreet())
                .build();
    }

    public LocationDto buildDto() {
        return LocationDto.builder()
                .build();
    }

    public Collection<LocationDto> buildAllDto(Collection<Location> model) {
        return model.stream()
                .map(this::buildDto)
                .toList();
    }
}
