package com.github.lltal.observer.input.dto;

import java.util.Collection;

public record DutyResultDto(
        String fio,
        String username,
        String phoneNumber,
        Collection<TireResultDto> tires
) {
}
