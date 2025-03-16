package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.TireTypeSizeDto;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.exception.DuplicateValueException;
import com.github.lltal.observer.service.back.base.internal.TireTypeSizePrivateBackService;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.github.lltal.observer.config.constant.SenderName.TIRE_TYPE_SIZE_SENDER_NAME;

@Service
@RequiredArgsConstructor
public class TireTypeSizePrivateFrontService implements PrivateFrontService {
    private final TireTypeSizePrivateBackService tireTypeSizePrivateBackService;
    private final UiHelper helper;
    private final ContextParser parser;
    @Autowired
    @Qualifier(TIRE_TYPE_SIZE_SENDER_NAME)
    private AbstractSender sender;

    @Override
    public boolean isCreationService() {
        return true;
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.TYPE_SIZE;
    }

    @Override
    public Countable createManageableDto() {
        return new TireTypeSizeDto();
    }

    @Override
    public boolean isFullFill(Countable dto) {
        return dto.getCount() > 0;
    }

    @Override
    public void sendNextMessage(Countable dto, CommandContext context) {
        helper.sendMessage(
                context,
                getNextMessage((TireTypeSizeDto) dto, context)
        );
    }

    @Override
    public void fillDto(Countable manageableDto, CommandContext context) {
        switch (manageableDto.getCount()) {
            case 0 -> execTypeSize((TireTypeSizeDto) manageableDto, context);
            default -> throw new IllegalStateException("count out of range during filling");
        }
    }

    private BotApiMethod<?> getNextMessage(TireTypeSizeDto dto, CommandContext context) {
        return switch (dto.getCount()) {
            case 0 -> sender.getNextMessage(dto, parser.getChatId(context));
            case 1 -> sender.getNextMessage(dto, parser.getChatId(context));
            default -> throw new IllegalStateException("count out range during message creation");
        };
    }

    private void execTypeSize(TireTypeSizeDto dto, CommandContext context) {
        dto.setTireSize(context.getName());
        dto.setCount(dto.getCount() + 1);

        if (tireTypeSizePrivateBackService.contains(dto.getTireSize()))
            throw new DuplicateValueException(
                    String.format("типо-размер %s уже сущесвует", dto.getTireSize())
            );

        tireTypeSizePrivateBackService.save(dto);
    }
}
