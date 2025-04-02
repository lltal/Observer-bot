package com.github.lltal.observer.service.back.converter;

import com.github.lltal.observer.input.dto.TireResultDto;
import com.github.lltal.observer.model.Tire;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TireConverter {

    public TireResultDto buildTireDto(Tire tire) {
        return new TireResultDto(
                tire.getCarNumber(),
                tire.getTireModel().getMark().getName(),
                tire.getTireModel().getName(),
                tire.getTypeSize().getTireSize()
        );
    }

    public Collection<TireResultDto> buildAllDtos(Collection<Tire> tires) {
        return tires.stream().map(this::buildTireDto).toList();
    }
}
