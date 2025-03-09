package com.github.lltal.observer.services.model;

import com.github.lltal.observer.input.dto.TireTypeSizeDto;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.output.TireTypeSizeRepo;
import com.github.lltal.observer.services.builder.TireTypeSizeBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TireTypeSizeService implements ModelService<TireTypeSizeDto, String> {
    private final TireTypeSizeRepo repo;
    private final TireTypeSizeBuilder builder;

    @Override
    public void create(TireTypeSizeDto dto) {
        repo.save(builder.buildModel(dto));
    }

    @Override
    public TireTypeSizeDto find(String tireSize) {
        return builder.buildDto(repo.findByTireSize(tireSize));
    }

    @Override
    public Collection<TireTypeSizeDto> findAll() {
        return builder.buildAllDto(repo.findAll());
    }

    @Override
    public void delete(String tireSize) {
        repo.deleteByTireSize(tireSize);
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.TYPE_SIZE;
    }

    public Collection<String> findAllTireSize() {
        return repo.findAllTireSize();
    }
}
