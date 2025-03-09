package com.github.lltal.observer.services.builder;

import com.github.lltal.observer.entity.TireModel;
import com.github.lltal.observer.input.dto.TireModelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TireModelBuilder {
    private TireMarkBuilder markBuilder;

    public TireModel buildModel(TireModelDto dto) {
        TireModel tireModel = new TireModel();
        tireModel.setCode(dto.getCode());
        tireModel.setName(dto.getName());
        tireModel.setMark(markBuilder.buildModel(dto.getMarkDto()));
        return tireModel;
    }

    public TireModelDto buildDto() {
        return TireModelDto.builder().build();
    }


    public TireModelDto buildDto(TireModel model) {
        return TireModelDto.builder()
                .code(model.getCode())
                .name(model.getName())
                .build();
    }

    public Collection<TireModelDto> buildAllDto(Collection<TireModel> model) {
        return model.stream()
                .map(this::buildDto)
                .toList();
    }

    public Collection<TireModel> buildAllModel(Collection<TireModelDto> dto) {
        return dto.stream()
                .map(this::buildModel)
                .toList();
    }
}
