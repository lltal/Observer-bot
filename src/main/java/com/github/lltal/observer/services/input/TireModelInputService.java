package com.github.lltal.observer.services.input;

import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.TireMarkDto;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.services.builder.TireModelBuilder;
import com.github.lltal.observer.services.model.TireMarkService;
import com.github.lltal.observer.services.model.TireModelService;
import com.github.lltal.observer.services.parser.ContextParser;
import com.github.lltal.observer.services.ui.UiHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Collection;

import static com.github.lltal.observer.input.constant.TireModelConstants.TIRE_MODEL_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.TireModelConstants.TIRE_MODEL_SENDER_NAME;

@Service
public class TireModelInputService implements InputService{
    @Autowired
    private TireModelBuilder builder;
    @Autowired
    @Qualifier(TIRE_MODEL_SENDER_NAME)
    private AbstractSender sender;
    @Autowired
    @Qualifier(TIRE_MODEL_RESOLVER_NAME)
    private AbstractResolver resolver;
    @Autowired
    private TireModelService modelService;
    @Autowired
    private TireMarkService markService;
    @Autowired
    private UiHelper helper;
    @Autowired
    private ContextParser parser;

    @Override
    public AdminActionObjectType getActionObjectType() {
        return AdminActionObjectType.MODEL;
    }

    @Override
    public Countable createManageableDto() {
        return builder.buildDto();
    }

    @Override
    public BotApiMethod<?> getNextCreationMessage(Countable manageableDto, CommandContext context) {
        if (manageableDto.getCount() == 2) {
            Collection<TireMarkDto> marks = markService.findAll();

            helper.createKeyboard("Выбери марку", parser.getChatId(context), )
            TireMarkDto markDto = markService.find(context.getName());
            manageableDto
        }
        return sender.getNextMessage(manageableDto, parser.getChatId(context));
    }

    @Override
    public BotApiMethod<?> getNextDeletionMessage(Long chatId) {
        return null;
    }

    @Override
    public void fillDto(Countable manageableDto, CommandContext context) {

        resolver.resolve(manageableDto, context);
    }

    @Override
    public BotApiMethod<?> delete(CommandContext context, Long chatId) {
        return null;
    }
}

