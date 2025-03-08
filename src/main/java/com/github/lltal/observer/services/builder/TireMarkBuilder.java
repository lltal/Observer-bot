package com.github.lltal.observer.services.builder;

import com.github.lltal.observer.entity.TireMark;
import com.github.lltal.observer.input.dto.TireMarkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TireMarkBuilder {
    private final TireModelBuilder modelBuilder;

    public TireMark buildModel(TireMarkDto dto) {
        TireMark tireMark = new TireMark();
        tireMark.setModels(
                modelBuilder.buildAllModel(dto.getModels())
        );
        tireMark.setName(dto.getName());
        return tireMark;
    }

    public TireMarkDto buildDto() {
        return TireMarkDto.builder().build();
    }

    public TireMarkDto buildDto(TireMark model) {
        TireMarkDto markDto = new TireMarkDto();
        markDto.setName(model.getName());
        markDto.setModels(modelBuilder.buildAllDto(model.getModels()));
        return markDto;
    }

    public Collection<TireMarkDto> buildAllDto(Collection<TireMark> model) {
        return model.stream()
                .map(this::buildDto)
                .toList();
    }
}
