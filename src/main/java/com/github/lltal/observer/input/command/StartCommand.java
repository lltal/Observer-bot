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

@CommandNames("/start")
@Component
@Slf4j
@RequiredArgsConstructor
public class StartCommand {
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
            context.getEngine().executeNotException(
                    helper.createMessage(
                            chatId,
                            "Тебя нет в белом листе пользователей бота, хуй соси"
                    )
            );
        } else {
            DutyDto dto = new DutyDto();
            userBotSession.setData(dto);
            dutyFrontService.sendNextMessage(dto, context);
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
}
