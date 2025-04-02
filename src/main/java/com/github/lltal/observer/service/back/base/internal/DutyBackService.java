package com.github.lltal.observer.service.back.base.internal;

import com.github.lltal.observer.input.dto.DutyDto;
import com.github.lltal.observer.input.dto.DutyResultDto;
import com.github.lltal.observer.model.Duty;
import com.github.lltal.observer.model.Tire;
import com.github.lltal.observer.model.User;
import com.github.lltal.observer.output.DutyRepo;
import com.github.lltal.observer.output.UserRepo;
import com.github.lltal.observer.service.back.base.BackService;
import com.github.lltal.observer.service.back.converter.DutyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class DutyBackService implements BackService {
    private final DutyRepo dutyRepo;
    private final UserRepo userRepo;
    private final TireBackService tireBackService;
    private final DutyConverter dutyConverter;

    public void save(DutyDto dutyDto) {
        User user = userRepo.findByTgId(dutyDto.getTgId());
        Duty duty = new Duty();
        duty.setCreatedAt(dutyDto.getCreatedAt());
        duty.setFio(dutyDto.getFio());
        duty.setUser(user);
        duty.setPhoneNumber(dutyDto.getPhoneNumber());
        duty.setTires(
                extractTires(dutyDto, duty)
        );
        dutyRepo.save(duty);
    }

    @Transactional
    public Collection<DutyResultDto> findAllByDate(Instant startDate, Instant endDate) {
        return dutyConverter.buildAllDtos(
                dutyRepo.findAllByDate(startDate, endDate)
        );
    }

    private Collection<Tire> extractTires(DutyDto dutyDto, Duty owner) {
        return dutyDto.getTires()
                .stream()
                .map(t -> tireBackService.createWithoutSave(t, owner))
                .toList();
    }
}
