package com.github.lltal.observer.services.base;

import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.DutyDto;
import com.github.lltal.observer.services.parser.ContextParser;
import com.github.lltal.observer.services.ui.UiHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.github.lltal.observer.input.constant.DutyConstants.DUTY_FILLER_NAME;
import static com.github.lltal.observer.input.constant.DutyConstants.DUTY_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.DutyConstants.DUTY_SENDER_NAME;

@Service
public class DutyService {

    @Autowired
    @Qualifier(DUTY_SENDER_NAME)
    private AbstractSender sender;
    @Autowired
    @Qualifier(DUTY_RESOLVER_NAME)
    private AbstractResolver resolver;
    @Autowired
    @Qualifier(DUTY_FILLER_NAME)
    private AbstractFiller filler;
    @Autowired
    private ContextParser parser;
    @Autowired
    private UiHelper helper;

    public BotApiMethod<?> getNextMessage(DutyDto dutyDto, CommandContext context) {
        return sender.getNextMessage(dutyDto, parser.getChatId(context));
    }

    public void fillDto(DutyDto dutyDto, CommandContext context) {
        if (dutyDto.getCount() == 2 && helper.matchPhoneNumber(context.getName())) {
            dutyDto.setCount(1);
            context.getEngine().executeNotException(
                    SendMessage.builder()
                            .chatId(parser.getChatId(context))
                            .text("Неверный формат номера телефона, попробуй еще")
                            .build()
            );
        }
        resolver.resolve(dutyDto, context);
    }
}
