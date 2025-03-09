package com.github.lltal.observer.services.parser;

import com.github.lltal.filler.starter.command.CommandContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateParser {

    public String getNewChatMemberStatus(Update update) {
        return update
                .getMyChatMember()
                .getNewChatMember()
                .getStatus();
    }

    public Long getForwardChatId(Update update) {
        return update
                .getMessage()
                .getForwardFromChat()
                .getId();
    }

    public String getMyChatMemberChatActionType(Update update) {
        return update
                .getMyChatMember()
                .getChat()
                .getType();
    }

    public Long getMyChatMemberChatId(Update update) {
        return update
                .getMyChatMember()
                .getChat()
                .getId();
    }

    public Long getMyChatMemberUserId(Update update) {
        return update
                .getMyChatMember()
                .getFrom()
                .getId();
    }

    public String getUserName(CommandContext context) {
        return context.getUpdate()
                .getMessage()
                .getChat()
                .getUserName();
    }
}
