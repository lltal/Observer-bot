package com.github.lltal.observer.input.handler;

import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.TireMarkDto;
import com.github.lltal.observer.services.model.TireMarkService;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component(TireMarkNameHandler.HANDLER_BEAN_NAME)
public class TireMarkNameHandler implements CustomFilleeHandler<TireMarkDto> {
    public static final String HANDLER_BEAN_NAME = "tireMarkNameHandler";
    @Autowired
    private TireMarkService markService;
    @Autowired
    private ContextParser contextParser;

    @Override
    public void handleField(TireMarkDto dto, CommandContext context) {
        markService.create(dto);
        context.getEngine().executeNotException(
                ok(
                    contextParser.getChatId(context)
                )
        );
    }

    private SendMessage ok(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Новая марка успешно добавлена")
                .build();
    }
}
