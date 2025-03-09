package com.github.lltal.observer.services.builder;

import com.github.lltal.observer.entity.TireModel;
import com.github.lltal.observer.input.dto.TireModelDto;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TireModelBuilder {

    public TireModel buildModel(TireModelDto dto) {
        TireModel tireModel = new TireModel();
        tireModel.setCode(dto.getCode());
        tireModel.setName(dto.getName());
        tireModel.setSeason(dto.getSeason());
        return tireModel;
    }

    public TireModelDto buildDto() {
        return TireModelDto.builder().build();
    }


    public TireModelDto buildDto(TireModel model) {
        return TireModelDto.builder()
                .code(model.getCode())
                .name(model.getName())
                .season(model.getSeason())
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
