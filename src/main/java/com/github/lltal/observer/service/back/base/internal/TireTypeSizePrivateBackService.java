package com.github.lltal.observer.service.back.base.internal;

import com.github.lltal.observer.input.dto.TireTypeSizeDto;
import com.github.lltal.observer.model.TireTypeSize;
import com.github.lltal.observer.output.TireTypeSizeRepo;
import com.github.lltal.observer.service.back.base.PrivateBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TireTypeSizePrivateBackService implements PrivateBackService {
    private final TireTypeSizeRepo repo;

    public void save(TireTypeSizeDto dto) {
        TireTypeSize tireTypeSize = new TireTypeSize();
        tireTypeSize.setTireSize(dto.getTireSize());

        repo.save(tireTypeSize);
    }

    public boolean contains(String typeSize) {
        return repo.existsByTireSize(typeSize);
    }

    @Transactional
    public void delete(String tireSize) {
        repo.deleteByTireSize(tireSize);
    }

    public Collection<String> findAllTireSize() {
        return repo.findAllTireSize();
    }
}
