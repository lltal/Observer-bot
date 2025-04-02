package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.config.constant.enumeration.YesNo;
import com.github.lltal.observer.config.constant.enumeration.converter.SeasonConverter;
import com.github.lltal.observer.config.constant.enumeration.converter.YesNoConverter;
import com.github.lltal.observer.input.dto.TireDto;
import com.github.lltal.observer.input.exception.WrongFormatException;
import com.github.lltal.observer.service.front.base.FrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.github.lltal.observer.config.constant.SenderName.TIRE_SENDER_NAME;


@Service
@Slf4j
@RequiredArgsConstructor
public class TireFrontService implements FrontService<TireDto> {
    private final ContextParser parser;
    private final TireMarkDeletionPrivateFrontService markFronService;
    private final TireModelDeletionPrivateFrontService modelDeletionFrontService;
    private final TireTypeSizeDeletionPrivateFrontService typeSizeFrontService;
    private final UiHelper helper;
    private final YesNoConverter yesNoConverter;
    private final SeasonConverter seasonConverter;
    @Autowired
    @Qualifier(TIRE_SENDER_NAME)
    private AbstractSender sender;

    @Override
    public boolean isFullFill(TireDto tireDto) {
        return tireDto.getCount() > 5;
    }

    @Override
    public void sendNextMessage(TireDto tireDto, CommandContext context) {
        helper.sendMessage(context, getNextMessage(tireDto, context));
    }

    @Override
    public void fillDto(TireDto tireDto, CommandContext context) {
        switch (tireDto.getCount()) {
            case 0 -> execCarNumber(tireDto, context);
            case 1 -> execMarkName(tireDto, context);
            case 2 -> execSeason(tireDto, context);
            case 3 -> execModelName(tireDto, context);
            case 4 -> execTypeSize(tireDto, context);
            case 5 -> execYesNo(tireDto, context);
            default -> throw new IllegalStateException(
                    "tireDto count must be equal or less then 6"
            );
        }
    }

    private BotApiMethod<?> getNextMessage(TireDto tireDto, CommandContext context) {
        return switch (tireDto.getCount()) {
            case 0 -> sender.getNextMessage(tireDto, parser.getChatId(context));
            case 1 -> markFronService.createMarkKeyboard(context);
            case 2 -> sender.getNextMessage(tireDto, parser.getChatId(context));
            case 3 -> modelDeletionFrontService.createModelKeyboard(context, tireDto.getMarkName(), tireDto.getSeason());
            case 4 -> typeSizeFrontService.createTypeSizeKeyboard(context);
            case 5 -> sender.getNextMessage(tireDto, parser.getChatId(context));
            case 6 -> sender.getNextMessage(tireDto, parser.getChatId(context));
            default -> throw new IllegalStateException(
                    String.format("tireDto count out of range = %d", tireDto.getCount())
            );
        };
    }

    private void execCarNumber(TireDto tireDto, CommandContext context) {
        try {
            if (!helper.matchCarNumber(context.getName()))
                throw new WrongFormatException("Неверный формат номера машины");
            tireDto.setCarNumber(context.getName());
            tireDto.setCount(tireDto.getCount() + 1);
        } catch (Exception e) {
            context.getEngine().executeNotException(
                    helper.createMessage(
                            parser.getChatId(context),
                            "Неверный формат автомобильного номера, попробуй еще раз"
                    )
            );
        }
    }

    private void execSeason(TireDto tireDto, CommandContext context) {
        helper.closeCb(context);
        tireDto.setSeason(
                seasonConverter.convertToEnum(
                        context.getName()
                )
        );
        tireDto.setCount(tireDto.getCount() + 1);
    }

    private void execMarkName(TireDto tireDto, CommandContext context) {
        helper.closeCb(context);
        tireDto.setMarkName(context.getName());
        tireDto.setCount(tireDto.getCount() + 1);
    }

    private void execModelName(TireDto tireDto, CommandContext context) {
        helper.closeCb(context);
        tireDto.setModelName(context.getName());
        tireDto.setCount(tireDto.getCount() + 1);
    }

    private void execTypeSize(TireDto tireDto, CommandContext context) {
        helper.closeCb(context);
        tireDto.setTypeSize(context.getName());
        tireDto.setCount(tireDto.getCount() + 1);
    }

    private void execYesNo(TireDto tireDto, CommandContext context) {
        helper.closeCb(context);
        tireDto.setCreateSuccessfully(
                yesNoConverter.convertToEnum(
                        context.getName()
                )
        );

        if (tireDto.getCreateSuccessfully() == YesNo.NO) {
            tireDto.setCount(10);
        }
    }
}
