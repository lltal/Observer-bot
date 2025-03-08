package com.github.lltal.observer.services.input;

import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.services.builder.TireTypeSizeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.github.lltal.observer.input.constant.TireTypeSizeConstants.TIRE_TYPE_SIZE_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.TireTypeSizeConstants.TIRE_TYPE_SIZE_SENDER_NAME;

public class TireTypeSizeInputService implements InputService {
    @Autowired
    private TireTypeSizeBuilder builder;
    @Autowired
    @Qualifier(TIRE_TYPE_SIZE_SENDER_NAME)
    private AbstractSender sender;
    @Autowired
    @Qualifier(TIRE_TYPE_SIZE_RESOLVER_NAME)
    private AbstractResolver resolver;

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.TYPE_SIZE;
    }

    @Override
    public Countable createManageableDto() {
        return builder.buildDto();
    }

    @Override
    public BotApiMethod<?> getNextMessage(Countable manageableDto, Long chatId) {
        return sender.getNextMessage(manageableDto, chatId);
    }

    @Override
    public void fillDto(Countable manageableDto, CommandContext context) {
        resolver.resolve(manageableDto, context);
    }
}
