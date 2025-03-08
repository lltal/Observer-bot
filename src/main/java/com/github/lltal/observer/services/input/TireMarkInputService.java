package com.github.lltal.observer.services.input;

import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.services.builder.TireMarkBuilder;
import com.github.lltal.observer.services.model.TireMarkService;
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

import static com.github.lltal.observer.input.constant.TireMarkConstants.TIRE_MARK_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.TireMarkConstants.TIRE_MARK_SENDER_NAME;

@Service
public class TireMarkInputService implements InputService {
    @Autowired
    private TireMarkBuilder builder;
    @Autowired
    @Qualifier(TIRE_MARK_SENDER_NAME)
    private AbstractSender sender;
    @Autowired
    @Qualifier(TIRE_MARK_RESOLVER_NAME)
    private AbstractResolver resolver;
    @Autowired
    private TireMarkService markService;
    @Autowired
    private UiHelper helper;
    @Autowired
    private ContextParser parser;

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.MARK;
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
    public BotApiMethod<?> getNextDeletionMessage(Long chatId) {
        Collection<String> names = markService.findAllNames();
        Collection<Supplier<String>> suppliers = new ArrayList<>();
        names.forEach((n) -> suppliers.add(() -> n));

        return helper.createKeyboard(
                "Какую марку удалить?",
                chatId,
                suppliers
        );
    }

    @Override
    public void fillDto(Countable manageableDto, CommandContext context) {
        resolver.resolve(manageableDto, context);
    }

    @Override
    public BotApiMethod<?> delete(CommandContext context) {
        helper.closeCb(context);
        String name = context.getName();
        markService.delete(name);

        return SendMessage.builder()
                .chatId(parser.getChatId(context))
                .text(
                        String.format("Марка с именем: \"%s\" успешно удалена", name)
                )
                .build();
    }
}
