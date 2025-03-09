package com.github.lltal.observer.services.model;

import com.github.lltal.observer.entity.TireMark;
import com.github.lltal.observer.entity.TireModel;
import com.github.lltal.observer.input.dto.TireModelDto;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.enumeration.Season;
import com.github.lltal.observer.output.TireMarkRepo;
import com.github.lltal.observer.output.TireModelRepo;
import com.github.lltal.observer.services.builder.TireModelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class TireModelService implements ModelService<TireModelDto, String> {
    @Autowired
    private TireModelRepo repo;
    @Autowired
    private TireMarkRepo markRepo;
    @Autowired
    private TireModelBuilder builder;

    @Override
    @Transactional
    public void create(TireModelDto dto) {
        TireModel model = builder.buildModel(dto);
        TireMark mark = markRepo.findByName(dto.getMarkDto().getName());
        model.setMark(mark);
        mark.getModels().add(model);
        markRepo.save(mark);
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

    @Transactional
    public void delete(TireModelDto dto) {
        repo.deleteByNameAndMarkAndSeason(
                dto.getName(),
                dto.getMarkDto().getName(),
                dto.getSeason()
        );
    }

    public Collection<String> findAllNames(String markName, Season season) {
        return repo.findAllNames(markName, season);
    }
}
