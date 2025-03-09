package com.github.lltal.observer.input.command;

import com.github.lltal.filler.starter.annotation.CommandFirst;
import com.github.lltal.filler.starter.annotation.CommandNames;
import com.github.lltal.filler.starter.annotation.CommandOther;
import com.github.lltal.filler.starter.annotation.ParamName;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.filler.starter.session.UserBotSession;
import com.github.lltal.observer.input.dto.DutyDto;
import com.github.lltal.observer.input.dto.TireDto;
import com.github.lltal.observer.services.base.DutyService;
import com.github.lltal.observer.services.base.TireService;
import com.github.lltal.observer.services.input.UserInputService;
import com.github.lltal.observer.services.parser.UpdateParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@CommandNames("/start")
@Component
@Slf4j
public class StartCommand {
    @Autowired
    private UserInputService userInputService;
    @Autowired
    private UpdateParser updateParser;
    @Autowired
    private DutyService dutyService;
    @Autowired
    private TireService tireService;

    @CommandFirst
    public void execFirst(
            CommandContext context,
            @ParamName("chatId") Long chatId,
            UserBotSession userBotSession
    ) {
        if (!userInputService.hasUser(
                updateParser.getUserName(context)
        )) {
            SendMessage.builder()
                    .chatId(chatId)
                    .text("Тебя нет в белом листе пользователей бота, хуй соси")
                    .build();
        }
        DutyDto dto = new DutyDto();
        context.getEngine().executeNotException(
            dutyService.getNextMessage(dto, context)
        );
    }

    @CommandOther
    public void execOther(
            CommandContext context,
            @ParamName("chatId") Long chatId,
            UserBotSession userBotSession
    ) {
        DutyDto dto = (DutyDto) userBotSession.getData();

        if (dto.getCount() <= 3) {
            dutyService.fillDto(dto, context);
            context.getEngine().executeNotException(
                    dutyService.getNextMessage(dto, context)
            );
        }

        try {
            TireDto tireDto = dto.getTire();
            context.getEngine().executeNotException(
                    tireService.getNextMessage(tireDto, context)
            );
            boolean isComplete = tireService.fillDto(tireDto, context);
            if (isComplete) {
                dto.setCount(3);
                context.getEngine().executeNotException(
                        dutyService.getNextMessage(dto, context)
                );
            }
        } catch (RuntimeException e) {
            log.error("Исключение во время выполнения команды /start", e);
            context.getEngine().executeNotException(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text("Что-то пошло не так. Попробуй начать заново (Введи /start)")
                            .build()
            );
            userBotSession.stop();
        }
    }
}
