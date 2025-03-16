package com.github.lltal.observer.service.front.base.internal;

import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.dto.LocationDto;
import com.github.lltal.observer.service.back.base.internal.LocationPrivateBackService;
import com.github.lltal.observer.service.front.base.PrivateFrontService;
import com.github.lltal.observer.service.front.ui.ContextParser;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.github.lltal.observer.config.constant.SenderName.LOCATION_SENDER_NAME;

@Service
@RequiredArgsConstructor
public class LocationPrivateFrontService implements PrivateFrontService {
    private final ContextParser parser;
    private final UiHelper helper;
    private final LocationPrivateBackService locationBackService;
    @Autowired
    @Qualifier(LOCATION_SENDER_NAME)
    private AbstractSender sender;

    @Override
    public boolean isCreationService() {
        return true;
    }

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.LOCATION;
    }

    @Override
    public Countable createManageableDto() {
        return new LocationDto();
    }

    @Override
    public boolean isFullFill(Countable dto) {
        return dto.getCount() > 0;
    }

    @Override
    public void sendNextMessage(Countable dto, CommandContext context) {
        helper.sendMessage(
                context,
                getNextCreationMessage(dto, context)
        );
    }

    @Override
    public void fillDto(Countable manageableDto, CommandContext context) {
        switch (manageableDto.getCount()) {
            case 0 -> execStreet((LocationDto) manageableDto, context);
            default -> throw new IllegalStateException("location count out of range on dto filling");
        }
    }

    private void execStreet(LocationDto dto, CommandContext context) {
        dto.setStreet(context.getName());
        dto.setCount(dto.getCount() + 1);

        locationBackService.save(dto);
    }

    private BotApiMethod<?> getNextCreationMessage(Countable dto, CommandContext context) {
        return sender.getNextMessage(dto, parser.getChatId(context));
    }
}
