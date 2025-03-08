package com.github.lltal.observer.services.model;

import com.github.lltal.observer.input.dto.TireMarkDto;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.output.TireMarkRepo;
import com.github.lltal.observer.services.builder.TireMarkBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TireMarkService implements ModelService<TireMarkDto, String> {
    private final TireMarkRepo repo;
    private final TireMarkBuilder builder;

    @Override
    public void create(TireMarkDto dto) {
        repo.save(builder.buildModel(dto));
    }

    @Override
    public TireMarkDto find(String name) {
        return builder.buildDto(repo.findByName(name));
    }

    @Override
    public Collection<TireMarkDto> findAll() {
        return builder.buildAllDto(repo.findAll());
    }

    @Override
    public void delete(String name) {
        repo.deleteByName(name);
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.MARK;
    }

    public Collection<String> findAllNames() {
        return repo.findAllName();
    }
}
