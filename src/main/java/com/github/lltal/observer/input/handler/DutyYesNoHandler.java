package com.github.lltal.observer.input.handler;

import com.github.lltal.converter.shared.ifc.AbstractConverter;
import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.constant.CommonConstants;
import com.github.lltal.observer.input.dto.DutyDto;
import com.github.lltal.observer.input.dto.TireDto;
import com.github.lltal.observer.input.enumeration.YesNo;
import com.github.lltal.observer.services.base.TireService;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.github.lltal.observer.input.constant.TireConstants.TIRE_FILLER_NAME;
import static com.github.lltal.observer.input.constant.TireConstants.TIRE_SENDER_NAME;

@Component(DutyYesNoHandler.HANDLER_BEAN_NAME)
public class DutyYesNoHandler implements CustomFilleeHandler<DutyDto> {
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
    @Autowired
    private TireService tireService;

    @Override
    public void handleField(DutyDto dto, CommandContext context) {
        sender.closeCb(context);
        filler.fill(dto, converter.convertToEnumValue(context.getName()));

        if (dto.getYesNo() == YesNo.CLOSE) {
            context.getEngine().executeNotException(
                    SendMessage.builder()
                            .text("Выполнение команды завершено")
                            .chatId(parser.getChatId(context))
                            .build()
            );
            context.getUserBotSession().stop();
        } else if (dto.getYesNo() == YesNo.NO) {
            context.getEngine().executeNotException(
                    SendMessage.builder()
                            .text("Смена завершена")
                            .chatId(parser.getChatId(context))
                            .build()
            );
            dto.setCount(0);
            context.getEngine().executeNotException(
                    sender.getNextMessage(dto, parser.getChatId(context))
            );
        } else {
            dto.setCount(4);
            dto.setTire(new TireDto());
            context.getEngine().executeNotException(
                    sender.getNextMessage(dto, parser.getChatId(context))
            );
            context.getEngine().executeNotException(
                    tireService.getNextMessage(dto.getTire(), context)
            );
        }
    }
}
