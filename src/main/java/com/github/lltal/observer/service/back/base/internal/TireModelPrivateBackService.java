package com.github.lltal.observer.service.back.base.internal;

import com.github.lltal.observer.config.constant.enumeration.Season;
import com.github.lltal.observer.input.dto.TireModelDeletionDto;
import com.github.lltal.observer.input.dto.TireModelDto;
import com.github.lltal.observer.model.TireMark;
import com.github.lltal.observer.model.TireModel;
import com.github.lltal.observer.output.TireMarkRepo;
import com.github.lltal.observer.output.TireModelRepo;
import com.github.lltal.observer.service.back.base.PrivateBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TireModelPrivateBackService implements PrivateBackService {
    private final TireModelRepo repo;
    private final TireMarkRepo markRepo;

    @Transactional
    public void save(TireModelDto dto) {
        TireModel model = new TireModel();

        model.setSeason(dto.getSeason());
        model.setName(dto.getName());
        model.setCode(dto.getCode());

        TireMark mark = markRepo.findByName(dto.getMarkName());
        model.setMark(mark);
        mark.getModels().add(model);

        markRepo.save(mark);
    }

    @Transactional
    public void delete(TireModelDeletionDto dto) {
        repo.deleteByNameAndMarkAndSeason(
                dto.getName(),
                dto.getMarkName(),
                dto.getSeason()
        );
    }

    public Collection<String> findAllNames(String markName, Season season) {
        return repo.findAllNames(markName, season);
    }
}
