package com.github.lltal.observer.service.front.base;

import com.github.lltal.filler.starter.command.CommandContext;

public interface FrontService<T> {
    boolean isFullFill(T dto);

    void sendNextMessage(T dto, CommandContext context);

    void fillDto(T dto, CommandContext context);
}
