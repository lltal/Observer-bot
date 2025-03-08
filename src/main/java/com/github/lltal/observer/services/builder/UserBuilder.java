package com.github.lltal.observer.services.builder;

import com.github.lltal.observer.entity.User;
import com.github.lltal.observer.input.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserBuilder {

    public User buildModel(UserDto dto) {
        return User.builder()
                .tgId(dto.getTgId())
                .build();
    }

    public UserDto buildDto(User model) {
        return UserDto.builder()
                .tgId(model.getTgId())
                .build();
    }

    public UserDto buildDto() {
        return UserDto.builder()
                .build();
    }


    public Collection<UserDto> buildAllDto(Collection<User> model) {
        return model.stream()
                .map(this::buildDto)
                .toList();
    }
}
