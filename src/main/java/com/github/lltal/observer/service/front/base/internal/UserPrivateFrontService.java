package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.UserDto;
import com.github.lltal.observer.config.constant.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.exception.DuplicateValueException;
import com.github.lltal.observer.service.back.base.internal.UserPrivateBackService;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.github.lltal.observer.config.constant.SenderName.USER_SENDER_NAME;

@Service
@RequiredArgsConstructor
public class UserPrivateFrontService implements PrivateFrontService {
    private final UserPrivateBackService userPrivateBackService;
    private final UiHelper helper;
    private final ContextParser parser;
    @Autowired
    @Qualifier(USER_SENDER_NAME)
    private AbstractSender sender;


    @Override
    public boolean isCreationService() {
        return true;
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.USER_ID;
    }

    @Override
    public Countable createManageableDto() {
        return new UserDto();
    }

    @Override
    public boolean isFullFill(Countable dto) {
        return dto.getCount() > 0;
    }

    @Override
    public void sendNextMessage(Countable dto, CommandContext context) {
        helper.sendMessage(
                context,
                getNextMessage((UserDto) dto, context)
        );
    }

    @Override
    public void fillDto(Countable dto, CommandContext context) {
        switch (dto.getCount()) {
            case 0 -> execTgId((UserDto) dto, context);
            default -> throw new IllegalStateException("count out of range during filling");
        }
    }

    public boolean hasUser(String tgId) {
        return userPrivateBackService.contains(tgId);
    }

    private void execTgId(UserDto dto, CommandContext context) {
        dto.setTgId(context.getName());
        dto.setCount(dto.getCount() + 1);

        if (userPrivateBackService.contains(dto.getTgId()))
            throw new DuplicateValueException(
                    String.format("пользователь с id %s уже существует", dto.getTgId())
            );

        userPrivateBackService.save(dto);
    }

    private BotApiMethod<?> getNextMessage(UserDto dto, CommandContext context) {
        return switch (dto.getCount()) {
            case 0 -> sender.getNextMessage(dto, parser.getChatId(context));
            case 1 -> sender.getNextMessage(dto, parser.getChatId(context));
            default -> throw new IllegalStateException("count out range during message creation");
        };
    }


}
