package com.github.lltal.observer.services.parser;

import com.github.lltal.filler.starter.command.CommandContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContextParser {
    private final UpdateParser updateParser;

    public Long getChatId(CommandContext context) {
        return context.getChatBotSession().getChatId();
    }

    public Long getUserId(CommandContext context) {
        return context.getUserBotSession().getUserId();
    }

    public Long getForwardChatId(CommandContext context) {
        return updateParser.getForwardChatId(context.getUpdate());
    }
}

