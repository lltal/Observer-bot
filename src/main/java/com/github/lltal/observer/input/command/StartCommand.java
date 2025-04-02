package com.github.lltal.observer.input.command;

import com.github.lltal.filler.starter.annotation.CommandFirst;
import com.github.lltal.filler.starter.annotation.CommandNames;
import com.github.lltal.filler.starter.annotation.CommandOther;
import com.github.lltal.filler.starter.annotation.ParamName;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.filler.starter.session.UserBotSession;
import com.github.lltal.observer.input.dto.DutyDto;
import com.github.lltal.observer.input.dto.TireDto;
import com.github.lltal.observer.service.front.base.internal.DutyFrontService;
import com.github.lltal.observer.service.front.base.internal.TireFrontService;
import com.github.lltal.observer.service.front.base.internal.UserPrivateFrontService;
import com.github.lltal.observer.service.front.ui.UiHelper;
import com.github.lltal.observer.service.front.ui.UpdateParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

@CommandNames("/start")
@Component
@Slf4j
@RequiredArgsConstructor
public class StartCommand {
    public static final String MOSCOW_TIMEZONE = "Europe/Moscow";
    public static final int START_HOUR = 9;
    public static final int END_HOUR = 22;


    private final UserPrivateFrontService userInputService;
    private final UpdateParser updateParser;
    private final DutyFrontService dutyFrontService;
    private final TireFrontService tireFrontService;
    private final UiHelper helper;

    @CommandFirst
    public void execFirst(
            CommandContext context,
            @ParamName("chatId") Long chatId,
            UserBotSession userBotSession
    ) {
        if (!userInputService.hasUser(
                updateParser.getUserName(context)
            )
        ) {
            helper.sendMessage(
                    context,
                    helper.createMessage(
                            chatId,
                            "Тебя нет в белом листе пользователей бота, хуй соси"
                    )
            );
        } else {

            if (!isActive()) {
                helper.sendMessage(
                        context,
                        helper.createMessage(
                                chatId,
                                "Бот доступен с 22 до 9 по мск"
                        )
                );
            } else {
                DutyDto dto = new DutyDto();
                userBotSession.setData(dto);
                dutyFrontService.sendNextMessage(dto, context);
            }
        }
    }

    @CommandOther
    public void execOther(
            CommandContext context,
            @ParamName("chatId") Long chatId,
            UserBotSession userBotSession
    ) {
        try {
            DutyDto dutyDto = (DutyDto) userBotSession.getData();

            if (!dutyFrontService.isFullFill(dutyDto)) {
                dutyFrontService.fillDto(dutyDto, context);
                if (!dutyFrontService.isFullFill(dutyDto))
                    dutyFrontService.sendNextMessage(dutyDto, context);
                return;
            }

            TireDto tireDto = dutyDto.getTires().get(dutyDto.getTires().size() - 1);

            tireFrontService.fillDto(tireDto, context);
            if (!tireFrontService.isFullFill(tireDto))
                tireFrontService.sendNextMessage(tireDto, context);

            if (tireFrontService.isFullFill(tireDto)) {
                dutyFrontService.movePointerToYesNo(dutyDto);
                if (!dutyFrontService.isFullFill(dutyDto))
                    dutyFrontService.sendNextMessage(dutyDto, context);
            }

        } catch (RuntimeException e) {
            log.error("Исключение во время выполнения команды /start", e);
            context.getEngine().executeNotException(
                    helper.createMessage(
                            chatId,
                            "Что-то пошло не так. Попробуй начать заново"
                    )
            );
            DutyDto dutyDto = new DutyDto();
            userBotSession.setData(dutyDto);
            dutyFrontService.sendNextMessage(dutyDto, context);
        }
    }

private boolean isActive() {
        Instant now = Instant.now();
        LocalTime currentTime = LocalTime.ofInstant(now, ZoneId.of(MOSCOW_TIMEZONE).getRules().getOffset(now));

        LocalTime startTime = LocalTime.of(START_HOUR, 0);
        LocalTime endTime = LocalTime.of(END_HOUR, 0);

        return !(currentTime.isAfter(startTime) && currentTime.isBefore(endTime));
    }
}
