package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.config.constant.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.dto.TireTypeSizeDeletionDto;
import com.github.lltal.observer.input.exception.EmptyListException;
import com.github.lltal.observer.service.back.base.internal.TireTypeSizePrivateBackService;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Collection;

import static com.github.lltal.observer.config.constant.SenderName.TIRE_TYPE_SIZE_DELETION_SENDER_NAME;

@Service
@RequiredArgsConstructor
public class TireTypeSizeDeletionPrivateFrontService implements PrivateFrontService {
    private final TireTypeSizePrivateBackService tireTypeSizePrivateBackService;
    private final UiHelper helper;
    private final ContextParser parser;
    @Autowired
    @Qualifier(TIRE_TYPE_SIZE_DELETION_SENDER_NAME)
    private AbstractSender sender;

    @Override
    public boolean isFullFill(Countable dto) {
        return dto.getCount() > 0;
    }

    @Override
    public void sendNextMessage(Countable dto, CommandContext context) {
        helper.sendMessage(
                context,
                getNextMessage((TireTypeSizeDeletionDto) dto, context)
        );
    }

    @Override
    public void fillDto(Countable dto, CommandContext context) {
        switch (dto.getCount()) {
            case 0 -> execTypeSize((TireTypeSizeDeletionDto) dto, context);
            default -> throw new IllegalStateException("count out of range during filling");
        }
    }

    @Override
    public boolean isCreationService() {
        return false;
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.TYPE_SIZE;
    }

    @Override
    public Countable createManageableDto() {
        return new TireTypeSizeDeletionDto();
    }

    public BotApiMethod<?> createTypeSizeKeyboard(CommandContext context) {
        Collection<String> typeSizes = tireTypeSizePrivateBackService.findAllTireSize();

        if (typeSizes.isEmpty())
            throw new EmptyListException("Список размеров пуст");

        return helper.createKeyboard(
                "Выбери типоразмер",
                parser.getChatId(context),
                typeSizes
        );
    }

    private BotApiMethod<?> getNextMessage(TireTypeSizeDeletionDto dto, CommandContext context) {
        return switch (dto.getCount()) {
            case 0 -> createTypeSizeKeyboard(context);
            case 1 -> sender.getNextMessage(dto, parser.getChatId(context));
            default -> throw new IllegalStateException("count out of range during message creation");
        };
    }

    private void execTypeSize(TireTypeSizeDeletionDto dto, CommandContext context) {
        helper.closeCb(context);
        dto.setTireSize(context.getName());
        dto.setCount(dto.getCount() + 1);

        tireTypeSizePrivateBackService.delete(dto.getTireSize());
    }
}
