package com.github.lltal.observer.service.back.base.internal;

import com.github.lltal.observer.input.dto.TireMarkDto;
import com.github.lltal.observer.model.TireMark;
import com.github.lltal.observer.output.TireMarkRepo;
import com.github.lltal.observer.service.back.base.PrivateBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TireMarkPrivateBackService implements PrivateBackService {
    private final TireMarkRepo repo;

    public void save(TireMarkDto dto) {
        TireMark mark = new TireMark();
        mark.setName(dto.getName());
        repo.save(mark);
    }

    @Transactional
    public void delete(String name) {
        repo.deleteByName(name);
    }

    public Collection<String> findAllNames() {
        return repo.findAllName();
    }

    public boolean contains(String name) {
        return repo.existsByName(name);
    }
}
