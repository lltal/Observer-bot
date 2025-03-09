package com.github.lltal.observer.input.handler;

import com.github.lltal.converter.shared.ifc.AbstractConverter;
import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.constant.CommonConstants;
import com.github.lltal.observer.input.dto.AdminDto;
import com.github.lltal.observer.input.enumeration.YesNo;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_FILLER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_SENDER_NAME;

@Component(AdminYesNoHandler.HANDLER_BEAN_NAME)
public class AdminYesNoHandler implements CustomFilleeHandler<AdminDto> {
    public static final String HANDLER_BEAN_NAME = "adminYesNoHandler";

    @Qualifier(ACTION_FILLER_NAME)
    @Autowired
    private AbstractFiller filler;
    @Qualifier(ACTION_SENDER_NAME)
    @Autowired
    private AbstractSender sender;
    @Qualifier(CommonConstants.YES_NO_CONVERTER_NAME)
    @Autowired
    private AbstractConverter<YesNo> converter;
    @Autowired
    private ContextParser parser;

    @Override
    public void handleField(AdminDto dto, CommandContext context) {
        sender.closeCb(context);
        filler.fill(dto, converter.convertToEnumValue(context.getName()));

        BotApiMethod<?> message;
        if (dto.getYesNo() == YesNo.NO) {
            message = SendMessage.builder()
                    .text("Выполнение команды завершено")
                    .chatId(parser.getChatId(context))
                    .build();
            context.getUserBotSession().stop();
        } else
            message = sender.getNextMessage(dto, parser.getChatId(context));

        context.getEngine().executeNotException(
                message
        );
    }
}
