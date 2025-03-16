package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.dto.LocationDeletionDto;
import com.github.lltal.observer.input.exception.EmptyListException;
import com.github.lltal.observer.service.back.base.internal.LocationPrivateBackService;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Collection;

import static com.github.lltal.observer.config.constant.SenderName.LOCATION_DELETION_SENDER_NAME;

@Service
@RequiredArgsConstructor
public class LocationDeletionPrivateFrontService implements PrivateFrontService {
    private final LocationPrivateBackService locationBackService;
    private final ContextParser parser;
    private final UiHelper helper;
    @Autowired
    @Qualifier(LOCATION_DELETION_SENDER_NAME)
    private AbstractSender sender;

    @Override
    public boolean isFullFill(Countable dto) {
        return dto.getCount() > 0;
    }

    @Override
    public void sendNextMessage(Countable dto, CommandContext context) {
        helper.sendMessage(
                context,
                getNextMessage(dto, context)
        );
    }

    @Override
    public void fillDto(Countable dto, CommandContext context) {
        switch (dto.getCount()) {
            case 0 -> execStreetKeyboard((LocationDeletionDto) dto, context);
            default -> throw new IllegalStateException("location count out of range on deletion fill");
        }
    }

    @Override
    public boolean isCreationService() {
        return false;
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.LOCATION;
    }

    @Override
    public Countable createManageableDto() {
        return new LocationDeletionDto();
    }

    private void execStreetKeyboard(LocationDeletionDto dto, CommandContext context) {
        helper.closeCb(context);
        dto.setStreet(context.getName());
        dto.setCount(dto.getCount() + 1);

        locationBackService.delete(dto.getStreet());
    }

    private BotApiMethod<?> getNextMessage(Countable dto, CommandContext context) {
        return switch (dto.getCount()) {
            case 0 -> createStreetKeyboard(context);
            case 1 -> sender.getNextMessage(dto, parser.getChatId(context));
            default -> throw new IllegalStateException("location count out of range on deletion message creation");
        };
    }

    private BotApiMethod<?> createStreetKeyboard(CommandContext context) {
        Collection<String> streets = locationBackService.findAllStreets();

        if (streets.isEmpty())
            throw new EmptyListException("Список улиц пуст");

        return helper.createKeyboard(
                "Выбери улицу",
                parser.getChatId(context),
                streets
        );
    }
}
