package com.github.lltal.observer.services.input;

import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.LocationDto;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.services.builder.LocationBuilder;
import com.github.lltal.observer.services.model.LocationService;
import com.github.lltal.observer.services.parser.ContextParser;
import com.github.lltal.observer.services.ui.UiHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_SENDER_NAME;

@Service
public class LocationInputService implements InputService{
    @Autowired
    private LocationBuilder builder;
    @Autowired
    @Qualifier(LOCATION_SENDER_NAME)
    private AbstractSender sender;
    @Autowired
    @Qualifier(LOCATION_RESOLVER_NAME)
    private AbstractResolver resolver;
    @Autowired
    private LocationService locationService;
    @Autowired
    private UiHelper helper;
    @Autowired
    private ContextParser parser;

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.LOCATION;
    }

    @Override
    public Countable createManageableDto() {
        return builder.buildDto();
    }

    @Override
    public BotApiMethod<?> getNextCreationMessage(Countable manageableDto, CommandContext context) {
        return sender.getNextMessage(manageableDto, parser.getChatId(context));
    }

    @Override
    public BotApiMethod<?> getNextDeletionMessage(Long chatId) {
        Collection<LocationDto> marks = locationService.findAll();
        Collection<Supplier<String>> suppliers = new ArrayList<>();
        marks.forEach((l) -> suppliers.add(l::getStreet));

        return helper.createKeyboard(
                "Какую марку удалить?",
                chatId,
                suppliers
        );
    }

    @Override
    public void fillDto(Countable manageableDto, CommandContext context) {
        resolver.resolve(manageableDto, context);
    }

    @Override
    public BotApiMethod<?> delete(CommandContext context) {
        helper.closeCb(context);
        String street = context.getName();
        locationService.delete(street);

        return SendMessage.builder()
                .chatId(parser.getChatId(context))
                .text(
                        String.format("Адрес: \"%s\" успешно удалена", street)
                )
                .build();
    }
}
