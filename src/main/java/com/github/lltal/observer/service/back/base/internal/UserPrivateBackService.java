package com.github.lltal.observer.service.back.base.internal;

import com.github.lltal.observer.input.dto.UserDto;
import com.github.lltal.observer.model.User;
import com.github.lltal.observer.output.UserRepo;
import com.github.lltal.observer.service.back.base.PrivateBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserPrivateBackService implements PrivateBackService {
    private final UserRepo repo;

    public void save(UserDto dto) {
        User user = new User();
        user.setTgId(dto.getTgId());
        repo.save(user);
    }

    @Transactional
    public void delete(String tgId) {
        repo.deleteByTgId(tgId);
    }

    public Collection<String> findAllTgId() {
        return repo.findAllTgId();
    }

    public boolean contains(String tgId) {
        return repo.existsByTgId(tgId);
    }
}
