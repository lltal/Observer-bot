package com.github.lltal.observer.services.input;

import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.enumeration.AdminActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface InputService {
    @Autowired
    default void regService(InputServices inputServices) {
        inputServices.putService(getActionObjectType(), this);
    }

    AdminActionObjectType getActionObjectType();

    Countable createManageableDto();

    BotApiMethod<?> getNextCreationMessage(Countable manageableDto, CommandContext context);

    BotApiMethod<?> getNextDeletionMessage(Countable manageableDto, CommandContext context);

    boolean fillDto(Countable manageableDto, CommandContext context);

    boolean deleteIfCan(Countable manageableDto, CommandContext context);
}
