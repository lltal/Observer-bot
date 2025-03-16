package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.enumeration.converter.SeasonConverter;
import com.github.lltal.observer.input.dto.TireModelDto;
import com.github.lltal.observer.service.back.base.internal.TireModelPrivateBackService;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.github.lltal.observer.config.constant.SenderName.TIRE_MODEL_SENDER_NAME;

@Service
@RequiredArgsConstructor
public class TireModelPrivateFrontService implements PrivateFrontService {
    private final TireMarkDeletionPrivateFrontService markFrontService;
    private final TireModelPrivateBackService modelBackService;
    private final UiHelper helper;
    private final ContextParser parser;
    private final SeasonConverter seasonConverter;
    @Autowired
    @Qualifier(TIRE_MODEL_SENDER_NAME)
    private AbstractSender sender;

    @Override
    public boolean isCreationService() {
        return true;
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.MODEL;
    }

    @Override
    public Countable createManageableDto() {
        return new TireModelDto();
    }

    @Override
    public boolean isFullFill(Countable dto) {
        return dto.getCount() > 3;
    }

    @Override
    public void sendNextMessage(Countable dto, CommandContext context) {
        helper.sendMessage(
                context,
                getNextMessage(dto, context)
        );
    }

    @Override
    public void fillDto(Countable manageableDto, CommandContext context) {
        switch (manageableDto.getCount()) {
            case 0 -> execMarkName((TireModelDto) manageableDto, context);
            case 1 -> execSeason((TireModelDto) manageableDto, context);
            case 2 -> execName((TireModelDto) manageableDto, context);
            case 3 -> execCode((TireModelDto) manageableDto, context);
            default -> throw new IllegalStateException("count out of range on creation");
        }

    }

    private void execMarkName(TireModelDto dto, CommandContext context) {
        dto.setMarkName(context.getName());
        dto.setCount(dto.getCount() + 1);
    }

    private void execSeason(TireModelDto dto, CommandContext context) {
        helper.closeCb(context);
        dto.setSeason(
                seasonConverter.convertToEnum(context.getName())
        );
        dto.setCount(dto.getCount() + 1);
    }

    private void execName(TireModelDto dto, CommandContext context) {
        dto.setName(context.getName());
        dto.setCount(dto.getCount() + 1);
    }

    private void execCode(TireModelDto dto, CommandContext context) {
        dto.setCode(Integer.parseInt(context.getName()));
        dto.setCount(dto.getCount() + 1);

        modelBackService.save(dto);
    }

    private BotApiMethod<?> getNextMessage(Countable dto, CommandContext context) {
        return switch (dto.getCount()) {
            case 0 -> markFrontService.createMarkKeyboard(context);
            case 1 -> sender.getNextMessage(dto, parser.getChatId(context));
            case 2 -> sender.getNextMessage(dto, parser.getChatId(context));
            case 3 -> sender.getNextMessage(dto, parser.getChatId(context));
            case 4 -> sender.getNextMessage(dto, parser.getChatId(context));
            default -> throw new IllegalStateException("count out of range on send message creation");
        };
    }
}

