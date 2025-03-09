package com.github.lltal.observer.services.model;

import com.github.lltal.observer.input.dto.UserDto;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.output.UserRepo;
import com.github.lltal.observer.services.base.ModelService;
import com.github.lltal.observer.services.builder.UserBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService implements ModelService<UserDto, String> {
    private final UserRepo repo;
    private final UserBuilder builder;

    @Override
    public void create(UserDto dto) {
        repo.save(builder.buildModel(dto));
    }

    @Override
    public UserDto find(String tgId) {
        return builder.buildDto(repo.findByTgId(tgId));
    }

    @Override
    public Collection<UserDto> findAll() {
        return builder.buildAllDto(repo.findAll());
    }

    @Override
    @Transactional
    public void delete(String tgId) {
        repo.deleteByTgId(tgId);
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.USER_ID;
    }

    public Collection<String> findAllTgId() {
        return repo.findAllTgId();
    }

    public boolean contains(String tgId) {
        return repo.existsByTgId(tgId);
    }
}
