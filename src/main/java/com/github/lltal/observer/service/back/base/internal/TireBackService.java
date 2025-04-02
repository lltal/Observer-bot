package com.github.lltal.observer.service.back.base.internal;

import com.github.lltal.observer.input.dto.TireDto;
import com.github.lltal.observer.model.Duty;
import com.github.lltal.observer.model.Tire;
import com.github.lltal.observer.model.TireModel;
import com.github.lltal.observer.model.TireTypeSize;
import com.github.lltal.observer.output.TireModelRepo;
import com.github.lltal.observer.output.TireTypeSizeRepo;
import com.github.lltal.observer.service.back.base.BackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TireBackService implements BackService {
    private final TireModelRepo tireModelRepo;
    private final TireTypeSizeRepo tireTypeSizeRepo;

    public Tire createWithoutSave(TireDto dto, Duty owner) {
        TireModel tireModel = tireModelRepo.findByNameMarkAndSeason(
                dto.getModelName(), dto.getMarkName(), dto.getSeason()
        );
        TireTypeSize typeSize = tireTypeSizeRepo.findByTireSize(dto.getTypeSize());
        Tire tire = new Tire();
        tire.setCreatedAt(dto.getCreatedAt());
        tire.setDuty(owner);
        tire.setCarNumber(dto.getCarNumber());
        tire.setTireModel(tireModel);
        tire.setTypeSize(typeSize);
        return tire;
    }
}
