package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.UserDeletionDto;
import com.github.lltal.observer.config.constant.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.exception.EmptyListException;
import com.github.lltal.observer.service.back.base.internal.UserPrivateBackService;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Collection;

import static com.github.lltal.observer.config.constant.SenderName.USER_DELETION_SENDER_NAME;

@Service
@RequiredArgsConstructor
public class UserDeletionPrivateFrontService implements PrivateFrontService {
    private final UserPrivateBackService userPrivateBackService;
    private final UiHelper helper;
    private final ContextParser parser;
    @Autowired
    @Qualifier(USER_DELETION_SENDER_NAME)
    private AbstractSender sender;


    @Override
    public boolean isFullFill(Countable dto) {
        return dto.getCount() > 0;
    }

    @Override
    public void sendNextMessage(Countable dto, CommandContext context) {
        helper.sendMessage(
                context,
                getNextMessage((UserDeletionDto) dto, context)
        );
    }

    @Override
    public void fillDto(Countable dto, CommandContext context) {
        switch (dto.getCount()) {
            case 0 -> execTgId((UserDeletionDto) dto, context);
            default -> throw new IllegalStateException("count out of range during filling");
        }
    }

    @Override
    public boolean isCreationService() {
        return false;
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.USER_ID;
    }

    @Override
    public Countable createManageableDto() {
        return new UserDeletionDto();
    }

    private void execTgId(UserDeletionDto dto, CommandContext context) {
        helper.closeCb(context);
        dto.setTgId(context.getName());
        dto.setCount(dto.getCount() + 1);

        userPrivateBackService.delete(dto.getTgId());
    }

    private BotApiMethod<?> getNextMessage(UserDeletionDto dto, CommandContext context) {
        return switch (dto.getCount()) {
            case 0 -> createUsersKeyboard(context);
            case 1 -> sender.getNextMessage(dto, parser.getChatId(context));
            default -> throw new IllegalStateException("count out range during message creation");
        };
    }

    private BotApiMethod<?> createUsersKeyboard(CommandContext context) {
        Collection<String> names = userPrivateBackService.findAllTgId();

        if (names.isEmpty())
            throw new EmptyListException("Список пользователей пуст");

        return helper.createKeyboard(
                "Выберите пользователя",
                parser.getChatId(context),
                names
        );
    }
}
