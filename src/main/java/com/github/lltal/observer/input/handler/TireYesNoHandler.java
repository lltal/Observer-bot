package com.github.lltal.observer.input.handler;

import com.github.lltal.converter.shared.ifc.AbstractConverter;
import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.constant.CommonConstants;
import com.github.lltal.observer.input.dto.AdminDto;
import com.github.lltal.observer.input.dto.TireDto;
import com.github.lltal.observer.input.enumeration.YesNo;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.github.lltal.observer.input.constant.TireConstants.TIRE_FILLER_NAME;
import static com.github.lltal.observer.input.constant.TireConstants.TIRE_SENDER_NAME;

@Component(TireYesNoHandler.HANDLER_BEAN_NAME)
public class TireYesNoHandler implements CustomFilleeHandler<TireDto> {
    public static final String HANDLER_BEAN_NAME = "tireYesNoHandler";

    @Qualifier(TIRE_FILLER_NAME)
    @Autowired
    private AbstractFiller filler;
    @Qualifier(TIRE_SENDER_NAME)
    @Autowired
    private AbstractSender sender;
    @Qualifier(CommonConstants.YES_NO_CONVERTER_NAME)
    @Autowired
    private AbstractConverter<YesNo> converter;
    @Autowired
    private ContextParser parser;

    @Override
    public void handleField(TireDto dto, CommandContext context) {
        sender.closeCb(context);
        filler.fill(dto, converter.convertToEnumValue(context.getName()));

        BotApiMethod<?> message;
        if (dto.getCreateSuccessfully() == YesNo.CLOSE) {
            message = SendMessage.builder()
                    .text("Выполнение команды завершено")
                    .chatId(parser.getChatId(context))
                    .build();
            context.getUserBotSession().stop();
        } else if (dto.getCreateSuccessfully() == YesNo.NO) {
            dto.setCount(0);
            message = sender.getNextMessage(dto, parser.getChatId(context));
        } else {
            message = sender.getNextMessage(dto, parser.getChatId(context));
        }

        context.getEngine().executeNotException(
                message
        );
    }
}
