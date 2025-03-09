package com.github.lltal.observer.services.model;

import com.github.lltal.observer.entity.TireMark;
import com.github.lltal.observer.input.dto.TireModelDto;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.output.TireModelRepo;
import com.github.lltal.observer.services.builder.TireModelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TireModelService implements ModelService<TireModelDto, String> {
    private final TireModelRepo repo;
    private final TireModelBuilder builder;

    @Override
    public void create(TireModelDto dto) {
        repo.save(builder.buildModel(dto));
    }

    @Override
    public TireModelDto find(String name) {
        return builder.buildDto(repo.findByName(name));
    }

    @Override
    public Collection<TireModelDto> findAll() {
        return builder.buildAllDto(repo.findAll());
    }

    @Override
    public void delete(String name) {
        repo.deleteByName(name);
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.MODEL;
    }

    public void delete(String name, TireMark mark) {
        repo.deleteByNameAndMark(name, mark);
    }
}
