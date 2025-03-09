package com.github.lltal.observer.input.handler;

import com.github.lltal.converter.shared.ifc.AbstractConverter;
import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.AdminDto;
import com.github.lltal.observer.input.enumeration.AdminActionType;
import com.github.lltal.observer.services.base.InputService;
import com.github.lltal.observer.services.input.InputServices;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_FILLER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_SENDER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_TYPE_CONVERTER_NAME;

@Component(AdminActionTypeHandler.HANDLER_BEAN_NAME)
public class AdminActionTypeHandler implements CustomFilleeHandler<AdminDto> {
    public static final String HANDLER_BEAN_NAME = "adminActionTypeHandler";

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
    @Qualifier(ACTION_TYPE_CONVERTER_NAME)
    private AbstractConverter<AdminActionType> converter;
    @Autowired
    private ContextParser parser;
    @Autowired
    private InputServices inputServices;

    @Override
    public void handleField(AdminDto dto, CommandContext context) {
        context.getEngine().executeNotException(sender.closeCb(context));

        filler.fill(dto, converter.convertToEnumValue(context.getName()));
        InputService service = inputServices.getService(dto.getObjectType());

        Countable manageableDto = service.createManageableDto();

        dto.setNewValue(manageableDto);

        BotApiMethod<?> message;
        if (dto.getActionType() == AdminActionType.ADD) {
            message = service.getNextCreationMessage(manageableDto, context);
        } else {
            message = service.getNextDeletionMessage(manageableDto, context);
        }

        context.getEngine().executeNotException(message);
    }
}
