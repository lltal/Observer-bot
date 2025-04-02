package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.TireMarkDeletionDto;
import com.github.lltal.observer.config.constant.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.exception.EmptyListException;
import com.github.lltal.observer.service.back.base.internal.TireMarkPrivateBackService;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Collection;

import static com.github.lltal.observer.config.constant.SenderName.TIRE_MARK_DELETION_SENDER_NAME;

@Service
@RequiredArgsConstructor
public class TireMarkDeletionPrivateFrontService implements PrivateFrontService {
    private final TireMarkPrivateBackService markPrivateBackService;
    private final UiHelper helper;
    private final ContextParser parser;
    @Autowired
    @Qualifier(TIRE_MARK_DELETION_SENDER_NAME)
    private AbstractSender sender;

    @Override
    public boolean isFullFill(Countable dto) {
        return dto.getCount() > 0;
    }

    @Override
    public void sendNextMessage(Countable dto, CommandContext context) {
        helper.sendMessage(
                context,
                getNextMessage((TireMarkDeletionDto) dto, context)
        );
    }

    @Override
    public void fillDto(Countable dto, CommandContext context) {
        switch (dto.getCount()) {
            case 0 -> execName((TireMarkDeletionDto) dto, context);
            case 1 -> throw new IllegalStateException("count out of range during dto filling");
        }
    }

    @Override
    public boolean isCreationService() {
        return false;
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.MARK;
    }

    @Override
    public Countable createManageableDto() {
        return new TireMarkDeletionDto();
    }

    public BotApiMethod<?> createMarkKeyboard(CommandContext context) {
        Collection<String> names = markPrivateBackService.findAllNames();

        if (names.isEmpty())
            throw new EmptyListException("Список марок пуст");

        return helper.createKeyboard(
                "Выбери марку",
                parser.getChatId(context),
                names
        );
    }

    private void execName(TireMarkDeletionDto dto, CommandContext context) {
        helper.closeCb(context);
        dto.setName(context.getName());
        dto.setCount(dto.getCount() + 1);

        markPrivateBackService.delete(dto.getName());
    }

    private BotApiMethod<?> getNextMessage(TireMarkDeletionDto dto, CommandContext context) {
        return switch (dto.getCount()) {
            case 0 -> createMarkKeyboard(context);
            case 1 -> sender.getNextMessage(dto, parser.getChatId(context));
            default -> throw new IllegalStateException("count out of range during message creation");
        };
    }
}
