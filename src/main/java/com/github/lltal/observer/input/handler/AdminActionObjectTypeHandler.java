package com.github.lltal.observer.input.handler;

import com.github.lltal.converter.shared.ifc.AbstractConverter;
import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.AdminDto;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_FILLER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_OBJECT_TYPE_CONVERTER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_SENDER_NAME;

@Component(AdminActionObjectTypeHandler.HANDLER_BEAN_NAME)
public class AdminActionObjectTypeHandler implements CustomFilleeHandler<AdminDto> {
    public static final String HANDLER_BEAN_NAME = "adminActionObjectTypeHandler";

    @Qualifier(ACTION_FILLER_NAME)
    @Autowired
    private AbstractFiller filler;
    @Autowired
    @Qualifier(ACTION_RESOLVER_NAME)
    private AbstractResolver resolver;
    @Autowired
    @Qualifier(ACTION_SENDER_NAME)
    private AbstractSender sender;
    @Autowired
    @Qualifier(ACTION_OBJECT_TYPE_CONVERTER_NAME)
    private AbstractConverter<AdminActionObjectType> converter;
    @Autowired
    private ContextParser parser;

    @Override
    public void handleField(AdminDto dto, CommandContext context) {
        context.getEngine().executeNotException(sender.closeCb(context));
        filler.fill(dto, converter.convertToEnumValue(context.getName()));
        BotApiMethod<?> message = sender.getNextMessage(dto, parser.getChatId(context));
        context.getEngine().executeNotException(message);
    }
}
