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
import com.github.lltal.observer.services.input.InputService;
import com.github.lltal.observer.services.input.InputServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_SENDER_NAME;

@CommandNames("/admin")
@Component
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

    @CommandFirst
    public void execFirst(
            CommandContext context,
            @ParamName("userId") Long userId,
            @ParamName("chatId") Long chatId,
            UserBotSession userBotSession
    ) {
        if (!properties.getAdmins().contains(userId)) {
            context.getEngine().executeNotException(forbid(chatId));
        }

        AdminDto dto = new AdminDto();
        userBotSession.setData(dto);
        BotApiMethod<?> message = sender.getNextMessage(dto, chatId);
        context.getEngine().executeNotException(message);
    }

    @CommandOther
    public void execOther(
            CommandContext context,
            @ParamName("chatId") Long chatId,
            UserBotSession userBotSession
    ) {
        AdminDto adminDto = (AdminDto) userBotSession.getData();

        if (adminDto.getNewValue() == null)
            resolver.resolve(adminDto, context);

        InputService service = inputServices.getService(adminDto.getObjectType());

        BotApiMethod<?> message;
        if (adminDto.getActionType() == AdminActionType.REMOVE)
            message = service.delete(context);
        else {
            service.fillDto(adminDto.getNewValue(), context);
            message = service.getNextCreationMessage(adminDto.getNewValue(), context);
        }

        context.getEngine().executeNotException(message);
    }

    private SendMessage forbid(Long chatId)
    {
        return SendMessage
                .builder()
                .chatId(chatId)
                .text("Ты не админ, хуй соси")
                .build();
    }
}
