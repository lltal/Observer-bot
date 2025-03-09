package com.github.lltal.observer.input.handler;

import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.UserDto;
import com.github.lltal.observer.input.exception.DuplicateValueException;
import com.github.lltal.observer.services.model.UserService;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.github.lltal.observer.input.constant.UserConstants.USER_FILLER_NAME;

@Component(UserTgIdHandler.HANDLER_BEAN_NAME)
public class UserTgIdHandler implements CustomFilleeHandler<UserDto> {
    public static final String HANDLER_BEAN_NAME = "userTgIdHandler";

    @Qualifier(USER_FILLER_NAME)
    @Autowired
    private AbstractFiller filler;
    @Autowired
    private UserService userService;
    @Autowired
    private ContextParser parser;

    @Override
    public void handleField(UserDto dto, CommandContext context) {
        filler.fill(dto, context.getName());
        if (userService.contains(dto.getTgId()))
            throw new DuplicateValueException(String.format("Имя пользователя %s уже существует", context.getName()));
        userService.create(dto);
    }
}
