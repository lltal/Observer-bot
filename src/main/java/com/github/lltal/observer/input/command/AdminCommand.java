package com.github.lltal.observer.input.command;

import com.github.lltal.filler.starter.annotation.CommandFirst;
import com.github.lltal.filler.starter.annotation.CommandNames;
import com.github.lltal.filler.starter.annotation.CommandOther;
import com.github.lltal.filler.starter.annotation.ParamName;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.filler.starter.session.UserBotSession;
import com.github.lltal.observer.config.BotProperties;
import com.github.lltal.observer.input.dto.AdminDto;
import com.github.lltal.observer.input.exception.DuplicateValueException;
import com.github.lltal.observer.input.exception.EmptyListException;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.base.PrivateFrontServices;
import com.github.lltal.observer.service.front.base.internal.AdminFrontService;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@CommandNames("/admin")
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminCommand {
    private final BotProperties properties;
    private final PrivateFrontServices privateFrontServices;
    private final UiHelper helper;
    private final AdminFrontService adminFrontService;

    @CommandFirst
    public void execFirst(
            CommandContext context,
            @ParamName("userId") Long userId,
            @ParamName("chatId") Long chatId,
            UserBotSession userBotSession
    ) {
        if (!properties.getAdmins().contains(userId)) {
            context.getEngine().executeNotException(
                    helper.createMessage(chatId, "Ты не админ, хуй соси")
            );
        } else {
            AdminDto dto = new AdminDto();
            userBotSession.setData(dto);
            adminFrontService.sendNextMessage(dto, context);
        }
    }

    @CommandOther
    public void execOther(
            CommandContext context,
            @ParamName("chatId") Long chatId,
            UserBotSession userBotSession
    ) {
        try {
            AdminDto adminDto = (AdminDto) userBotSession.getData();

            if (!adminFrontService.isFullFill(adminDto)) {
                adminFrontService.fillDto(adminDto, context);
                if (!adminFrontService.isFullFill(adminDto)) {
                    adminFrontService.sendNextMessage(adminDto, context);
                }
                return;
            }

            PrivateFrontService service =
                    privateFrontServices.getService(
                            adminDto.getActionType(),
                            adminDto.getObjectType()
                    );

            service.fillDto(adminDto.getNewValue(), context);
            if (!service.isFullFill(adminDto.getNewValue()))
                service.sendNextMessage(adminDto.getNewValue(), context);

            if (service.isFullFill(adminDto.getNewValue())) {
                adminDto = newAdminDto(userBotSession);
                adminFrontService.sendNextMessage(adminDto, context);
            }

        } catch (EmptyListException | DuplicateValueException e) {
            log.error("Exception during execution /admin command", e);
            context.getEngine().executeNotException(
                    helper.createMessage(chatId, e.getMessage())
            );
            AdminDto adminDto = newAdminDto(userBotSession);
            adminFrontService.sendNextMessage(adminDto, context);
        } catch (RuntimeException e) {
            log.error("Exception during execution /admin command", e);
            userBotSession.stop();
            context.getEngine().executeNotException(
                    helper.createMessage(
                            chatId,
                            "Выполнение команды завершено, возможно, ошибка в формате введенных данных"
                    )
            );
        }
    }

    private AdminDto newAdminDto(UserBotSession session) {
        AdminDto adminDto = new AdminDto();
        session.setData(adminDto);
        return adminDto;
    }
}
