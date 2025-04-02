package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.config.constant.enumeration.YesNo;
import com.github.lltal.observer.config.constant.enumeration.converter.YesNoConverter;
import com.github.lltal.observer.input.dto.DutyDto;
import com.github.lltal.observer.input.dto.TireDto;
import com.github.lltal.observer.input.exception.WrongFormatException;
import com.github.lltal.observer.service.back.base.internal.DutyBackService;
import com.github.lltal.observer.service.front.base.FrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import com.github.lltal.observer.service.front.ui.UpdateParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.github.lltal.observer.config.constant.SenderName.DUTY_SENDER_NAME;

@Service
@Slf4j
@RequiredArgsConstructor
public class DutyFrontService implements FrontService<DutyDto> {
    private final YesNoConverter yesNoConverter;
    private final ContextParser contextParser;
    private final UpdateParser updateParser;
    private final UiHelper helper;
    private final TireFrontService tireFrontService;
    private final DutyBackService dutyBackService;
    @Qualifier(DUTY_SENDER_NAME)
    @Autowired
    private AbstractSender sender;

    @Override
    public boolean isFullFill(DutyDto dutyDto) {
        return dutyDto.getCount() > 3;
    }

    @Override
    public void sendNextMessage(DutyDto dutyDto, CommandContext context) {
        helper.sendMessage(context, getNextMessage(dutyDto, context));
    }

    @Override
    public void fillDto(DutyDto dutyDto, CommandContext context) {
        switch (dutyDto.getCount()) {
            case 0 -> execFio(dutyDto, context);
            case 2 -> execPhoneNumber(dutyDto, context);
            case 3 -> execYesNo(dutyDto, context);
            default -> throw new IllegalStateException("Exception during filling dutyDto");
        }
    }

    public void movePointerToYesNo(DutyDto dutyDto) {
        dutyDto.setCount(3);
    }

    private BotApiMethod<?> getNextMessage(DutyDto dutyDto, CommandContext context) {
        return switch (dutyDto.getCount()) {
            case 0 -> sender.getNextMessage(dutyDto, contextParser.getChatId(context));
            case 1 -> sender.getNextMessage(dutyDto, contextParser.getChatId(context));
            case 2 -> sender.getNextMessage(dutyDto, contextParser.getChatId(context));
            case 3 -> sender.getNextMessage(dutyDto, contextParser.getChatId(context));
            case 4 -> sender.getNextMessage(dutyDto, contextParser.getChatId(context));
            default -> throw new IllegalStateException();
        };
    }

    private void execFio(DutyDto dutyDto, CommandContext context) {
        dutyDto.setFio(getFioFromContext(context));
        dutyDto.setTgId(updateParser.getUserName(context));
        dutyDto.setCount(dutyDto.getCount() + 2);
    }

    private void execPhoneNumber(DutyDto dutyDto, CommandContext context) {
        try {
            if (!helper.matchPhoneNumber(context.getName()))
                throw new WrongFormatException("Неверный формат номера телефона");
            dutyDto.setPhoneNumber(context.getName());
            dutyDto.setCount(dutyDto.getCount() + 1);
        } catch (Exception e) {
            context.getEngine().executeNotException(
                    helper.createMessage(
                            contextParser.getChatId(context),
                            "Неверный формат номера телефона, попробуй еще"
                    )
            );
        }
    }

    private void execYesNo(DutyDto dutyDto, CommandContext context) {
        helper.closeCb(context);

        dutyDto.setYesNo(
                yesNoConverter.convertToEnum(
                        context.getName()
                )
        );
        dutyDto.setCount(dutyDto.getCount() + 1);

        if (dutyDto.getYesNo() == YesNo.NO) {
            no(dutyDto, context);
        } else {
            yes(dutyDto, context);
        }
    }

    private void yes(DutyDto dutyDto, CommandContext context) {

        TireDto tireDto = new TireDto();
        dutyDto.getTires().add(tireDto);

        sendNextMessage(dutyDto, context);
        tireFrontService.sendNextMessage(tireDto, context);
    }

    private void no(DutyDto dutyDto, CommandContext context) {
        context.getEngine().executeNotException(
                SendMessage.builder()
                        .text("Смена завершена")
                        .chatId(contextParser.getChatId(context))
                        .build()
        );

        dutyBackService.save(dutyDto);

        dutyDto.setCount(5);

        dutyDto = new DutyDto();
        context.getUserBotSession().setData(dutyDto);

        sendNextMessage(dutyDto, context);
    }

    private String getFioFromContext(CommandContext context) {
        return String.join(" ", (String[]) context.getData());
    }
}
