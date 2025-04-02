package com.github.lltal.observer.service.back.converter;

import com.github.lltal.observer.input.dto.DutyResultDto;
import com.github.lltal.observer.model.Duty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class DutyConverter {
    private final TireConverter tireConverter;

    public DutyResultDto buildDutyDto(Duty duty) {
        return new DutyResultDto(
                duty.getFio(),
                duty.getUser().getTgId(),
                duty.getPhoneNumber(),
                tireConverter.buildAllDtos(duty.getTires())
        );
    }

    public Collection<DutyResultDto> buildAllDtos(Collection<Duty> duties) {
        return duties.stream().map(this::buildDutyDto).toList();
    }
}
