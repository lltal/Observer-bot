package com.github.lltal.observer.services.input;

import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.TireTypeSizeDto;
import com.github.lltal.observer.input.dto.UserDto;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.exception.EmptyListException;
import com.github.lltal.observer.services.builder.UserBuilder;
import com.github.lltal.observer.services.model.UserService;
import com.github.lltal.observer.services.parser.ContextParser;
import com.github.lltal.observer.services.ui.UiHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

import static com.github.lltal.observer.input.constant.UserConstants.USER_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.UserConstants.USER_SENDER_NAME;

@Service
public class UserInputService implements InputService {
    @Autowired
    private UserBuilder builder;
    @Autowired
    @Qualifier(USER_SENDER_NAME)
    private AbstractSender sender;
    @Autowired
    @Qualifier(USER_RESOLVER_NAME)
    private AbstractResolver resolver;
    @Autowired
    private UserService userService;
    @Autowired
    private UiHelper helper;
    @Autowired
    private ContextParser parser;

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.USER_ID;
    }

    @Override
    public Countable createManageableDto() {
        return builder.buildDto();
    }

    @Override
    public BotApiMethod<?> getNextCreationMessage(Countable manageableDto, CommandContext context) {
        return sender.getNextMessage(manageableDto, parser.getChatId(context));
    }

    @Override
    public BotApiMethod<?> getNextDeletionMessage(Countable manageableDto, CommandContext context) {
        if (manageableDto.getCount() == 0) {
            return createUsersKeyboard(context);
        } else if (manageableDto.getCount() == 1)
            helper.closeCb(context);

        return SendMessage.builder()
                .chatId(parser.getChatId(context))
                .text(
                        String.format("Пользователь с id: \"%s\" успешно удален", context.getName())
                )
                .build();
    }

    @Override
    public boolean fillDto(Countable manageableDto, CommandContext context) {
        resolver.resolve(manageableDto, context);
        return manageableDto.getCount() >= 1;
    }

    @Override
    public boolean deleteIfCan(Countable manageableDto, CommandContext context) {
        if (manageableDto.getCount() == 0) {
            ((UserDto) manageableDto).setTgId(context.getName());
            manageableDto.setCount(manageableDto.getCount() + 1);
            userService.delete(context.getName());
            return true;
        }
        return false;
    }

    private BotApiMethod<?> createUsersKeyboard(CommandContext context) {
        Collection<String> names = userService.findAllTgId();
        Collection<Supplier<String>> suppliers = new ArrayList<>();
        names.forEach((n) -> suppliers.add(() -> n));

        if (names.isEmpty())
            throw new EmptyListException("Список пользователей пуст");

        return helper.createKeyboard(
                "Выберите пользователя",
                parser.getChatId(context),
                suppliers
        );
    }
}
