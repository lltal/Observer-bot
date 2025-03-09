package com.github.lltal.observer.input.command;

import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.starter.annotation.CommandFirst;
import com.github.lltal.filler.starter.annotation.CommandNames;
import com.github.lltal.filler.starter.annotation.CommandOther;
import com.github.lltal.filler.starter.annotation.ParamName;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.filler.starter.session.UserBotSession;
import com.github.lltal.observer.config.BotProperties;
import com.github.lltal.observer.input.dto.AdminDto;
import com.github.lltal.observer.input.enumeration.AdminActionType;
import com.github.lltal.observer.input.exception.DuplicateValueException;
import com.github.lltal.observer.input.exception.EmptyListException;
import com.github.lltal.observer.services.base.InputService;
import com.github.lltal.observer.services.input.InputServices;
import com.github.lltal.observer.services.ui.UiHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_SENDER_NAME;

@CommandNames("/admin")
@Component
@Slf4j
public class AdminCommand {
    @Autowired
    private BotProperties properties;
    @Autowired
    @Qualifier(ACTION_RESOLVER_NAME)
    private AbstractResolver resolver;
    @Autowired
    @Qualifier(ACTION_SENDER_NAME)
    private AbstractSender sender;
    @Autowired
    private InputServices inputServices;
    @Autowired
    private UiHelper helper;

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
        }

        AdminDto dto = new AdminDto();
        userBotSession.setData(dto);
        context.getEngine().executeNotException(
                sender.getNextMessage(dto, chatId)
        );
    }

    @CommandOther
    public void execOther(
            CommandContext context,
            @ParamName("chatId") Long chatId,
            UserBotSession userBotSession
    ) {
        AdminDto adminDto = (AdminDto) userBotSession.getData();

        try {
            if (adminDto.getCount() < 3) {
                resolver.resolve(adminDto, context);
                return;
            }

            InputService service = inputServices.getService(adminDto.getObjectType());

            BotApiMethod<?> message;
            boolean isComplete;
            if (adminDto.getActionType() == AdminActionType.REMOVE) {
                isComplete = service.deleteIfCan(adminDto.getNewValue(), context);
                message = service.getNextDeletionMessage(adminDto.getNewValue(), context);
            }
            else {
                isComplete = service.fillDto(adminDto.getNewValue(), context);
                message = service.getNextCreationMessage(adminDto.getNewValue(), context);
            }

            context.getEngine().executeNotException(message);

            if (isComplete) {
                adminDto.setCount(0);
                context.getEngine().executeNotException(
                        sender.getNextMessage(adminDto, chatId)
                );
            }

        } catch (EmptyListException | DuplicateValueException e) {
            log.error("Exception during execution /admin command", e);
            context.getEngine().executeNotException(
                    helper.createMessage(chatId, e.getMessage())
            );
            adminDto.setCount(0);
            context.getEngine().executeNotException(
                    sender.getNextMessage(adminDto, chatId)
            );
        } catch (RuntimeException e) {
            log.error("Exception during execution /admin command", e);
            context.getEngine().executeNotException(
                    helper.createMessage(chatId, "Выполнение команды завершено, возможно, ошибка в формате введенных данных")
            );
            userBotSession.stop();
        }
    }
}
