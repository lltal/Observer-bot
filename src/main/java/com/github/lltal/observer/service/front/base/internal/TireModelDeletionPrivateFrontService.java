package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.config.constant.enumeration.AdminActionObjectType;
import com.github.lltal.observer.config.constant.enumeration.Season;
import com.github.lltal.observer.config.constant.enumeration.converter.SeasonConverter;
import com.github.lltal.observer.input.dto.TireModelDeletionDto;
import com.github.lltal.observer.input.exception.EmptyListException;
import com.github.lltal.observer.service.back.base.internal.TireModelPrivateBackService;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Collection;

import static com.github.lltal.observer.config.constant.SenderName.TIRE_MODEL_DELETION_SENDER_NAME;

@Service
@RequiredArgsConstructor
public class TireModelDeletionPrivateFrontService implements PrivateFrontService {
    private final TireModelPrivateBackService modelBackService;
    private final TireMarkDeletionPrivateFrontService markFrontService;
    private final UiHelper helper;
    private final ContextParser parser;
    private final SeasonConverter seasonConverter;
    @Autowired
    @Qualifier(TIRE_MODEL_DELETION_SENDER_NAME)
    private AbstractSender sender;

    @Override
    public boolean isCreationService() {
        return false;
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.MODEL;
    }

    @Override
    public Countable createManageableDto() {
        return new TireModelDeletionDto();
    }

    @Override
    public boolean isFullFill(Countable dto) {
        return dto.getCount() > 2;
    }

    @Override
    public void sendNextMessage(Countable dto, CommandContext context) {
        helper.sendMessage(
                context,
                getNextMessage((TireModelDeletionDto) dto, context)
        );
    }

    @Override
    public void fillDto(Countable dto, CommandContext context) {
        switch (dto.getCount()) {
            case 0 -> execMarkName((TireModelDeletionDto) dto, context);
            case 1 -> execSeason((TireModelDeletionDto) dto, context);
            case 2 -> execName((TireModelDeletionDto) dto, context);
            default -> throw new IllegalStateException("count out of range on deletion creation");
        }
    }

    public BotApiMethod<?> createModelKeyboard(CommandContext context, String markName, Season season) {
        Collection<String> names = modelBackService.findAllNames(markName, season);

        if (names.isEmpty())
            throw new EmptyListException("Список моделей пуст");

        return helper.createKeyboard(
                "Выбери модель",
                parser.getChatId(context),
                names
        );
    }

    private BotApiMethod<?> getNextMessage(TireModelDeletionDto dto, CommandContext context) {
        return switch (dto.getCount()) {
            case 0 -> markFrontService.createMarkKeyboard(context);
            case 1 -> sender.getNextMessage(dto, parser.getChatId(context));
            case 2 -> createModelKeyboard(context, dto.getMarkName(), dto.getSeason());
            case 3 -> sender.getNextMessage(dto, parser.getChatId(context));
            default -> throw new IllegalStateException("count out of range in deletion message creation");
        };
    }

    private void execMarkName(TireModelDeletionDto manageableDto, CommandContext context) {
        helper.closeCb(context);
        manageableDto.setMarkName(context.getName());
        manageableDto.setCount(manageableDto.getCount() + 1);
    }

    private void execSeason(TireModelDeletionDto manageableDto, CommandContext context) {
        helper.closeCb(context);
        manageableDto.setSeason(
                seasonConverter.convertToEnum(context.getName())
        );
        manageableDto.setCount(manageableDto.getCount() + 1);
    }

    private void execName(TireModelDeletionDto manageableDto, CommandContext context) {
        helper.closeCb(context);
        manageableDto.setName(context.getName());
        manageableDto.setCount(manageableDto.getCount() + 1);

        modelBackService.delete(manageableDto);
    }
}
