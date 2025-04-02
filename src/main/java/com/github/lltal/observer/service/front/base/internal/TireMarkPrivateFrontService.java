package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.config.constant.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.dto.TireMarkDto;
import com.github.lltal.observer.input.exception.DuplicateValueException;
import com.github.lltal.observer.service.back.base.internal.TireMarkPrivateBackService;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.github.lltal.observer.config.constant.SenderName.TIRE_MARK_SENDER_NAME;

@Service
@RequiredArgsConstructor
public class TireMarkPrivateFrontService implements PrivateFrontService {
    private final TireMarkPrivateBackService markPrivateBackService;
    private final UiHelper helper;
    private final ContextParser parser;
    @Autowired
    @Qualifier(TIRE_MARK_SENDER_NAME)
    private AbstractSender sender;

    @Override
    public boolean isCreationService() {
        return true;
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.MARK;
    }

    @Override
    public Countable createManageableDto() {
        return new TireMarkDto();
    }

    @Override
    public boolean isFullFill(Countable dto) {
        return dto.getCount() > 0;
    }

    @Override
    public void sendNextMessage(Countable dto, CommandContext context) {
        helper.sendMessage(
                context,
                getNextMessage((TireMarkDto) dto, context)
        );
    }

    @Override
    public void fillDto(Countable manageableDto, CommandContext context) {
        switch (manageableDto.getCount()) {
            case 0 -> execName((TireMarkDto) manageableDto, context);
            default -> throw new IllegalStateException("count out of range during dto creation");
        }
    }

    private BotApiMethod<?> getNextMessage(TireMarkDto dto, CommandContext context) {
        return switch (dto.getCount()) {
            case 0 -> sender.getNextMessage(dto, parser.getChatId(context));
            case 1 -> sender.getNextMessage(dto, parser.getChatId(context));
            default -> throw new IllegalStateException("count out of range on message creation");
        };
    }

    private void execName(TireMarkDto dto, CommandContext context) {
        dto.setName(context.getName());
        dto.setCount(dto.getCount() + 1);

        if (markPrivateBackService.contains(dto.getName()))
            throw new DuplicateValueException(
                    String.format("марка с именем %s уже существует", dto.getName())
            );

        markPrivateBackService.save(dto);
    }
}
