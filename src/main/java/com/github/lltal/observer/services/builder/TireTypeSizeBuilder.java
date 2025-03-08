package com.github.lltal.observer.services.builder;

import com.github.lltal.observer.entity.TireTypeSize;
import com.github.lltal.observer.input.dto.TireTypeSizeDto;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TireTypeSizeBuilder {

    public TireTypeSize buildModel(TireTypeSizeDto dto) {
        return TireTypeSize.builder()
                .tireSize(dto.getTireSize())
                .build();
    }

    public TireTypeSizeDto buildDto(TireTypeSize model) {
        return TireTypeSizeDto.builder()
                .tireSize(model.getTireSize())
                .build();
    }

    public TireTypeSizeDto buildDto() {
        return TireTypeSizeDto.builder()
                .build();
    }

    public Collection<TireTypeSizeDto> buildAllDto(Collection<TireTypeSize> model) {
        return model.stream()
                .map(this::buildDto)
                .toList();
    }
}
