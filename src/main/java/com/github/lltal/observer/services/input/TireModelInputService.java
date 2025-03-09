package com.github.lltal.observer.services.input;

import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.TireMarkDto;
import com.github.lltal.observer.input.dto.TireModelDto;
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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

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
        if (manageableDto.getCount() == 0) {
            return createMarkSelectionMessage(context);
        } else if (manageableDto.getCount() == 1) {
            helper.closeCb(context);
        }

        return sender.getNextMessage(manageableDto, parser.getChatId(context));
    }

    @Override
    public BotApiMethod<?> getNextDeletionMessage(Countable manageableDto, CommandContext context) {
        if (manageableDto.getCount() == 0) {
            return createMarkSelectionMessage(context);
        }
        if (manageableDto.getCount() == 1) {
            helper.closeCb(context);
            return createModelSelectionMessage(context, (TireModelDto) manageableDto);
        } else if (manageableDto.getCount() == 2)
            helper.closeCb(context);

        return SendMessage.builder()
                .text("Модель успешно удалена")
                .chatId(parser.getChatId(context))
                .build();
    }

    @Override
    public boolean fillDto(Countable manageableDto, CommandContext context) {
        if (manageableDto.getCount() == 0) {
           setMark(manageableDto, context);
        } else if (manageableDto.getCount() == 3) {
            modelService.create((TireModelDto) manageableDto);
        }
        else
            resolver.resolve(manageableDto, context);

        return manageableDto.getCount() >= 3;
    }

    @Override
    public boolean deleteIfCan(Countable manageableDto, CommandContext context) {
        if (manageableDto.getCount() == 0) {
            setMark(manageableDto, context);
        } else if (manageableDto.getCount() == 1) {
            modelService.delete(
                    context.getName(),
                    markService.findWithoutConversion(
                            ((TireModelDto) manageableDto).getMarkDto().getName()
                    )
            );
            ((TireModelDto) manageableDto).setName(context.getName());
            manageableDto.setCount(manageableDto.getCount() + 1);
        }
        return manageableDto.getCount() >= 1;
    }

    private void setMark(Countable manageableDto, CommandContext context) {
        TireMarkDto markDto = markService.find(context.getName());
        ((TireModelDto)manageableDto).setMarkDto(markDto);
        manageableDto.setCount(manageableDto.getCount() + 1);
    }

    private BotApiMethod<?> createMarkSelectionMessage(CommandContext context) {
        Collection<String> names = markService.findAllNames();
        Collection<Supplier<String>> suppliers = new ArrayList<>();
        names.forEach((n) -> suppliers.add(() -> n));

        return helper.createKeyboard(
                "Выбери марку",
                parser.getChatId(context),
                suppliers
        );
    }

    private BotApiMethod<?> createModelSelectionMessage(CommandContext context, TireModelDto modelDto) {
        TireMarkDto markDto = markService.find(modelDto.getMarkDto().getName());
        Collection<Supplier<String>> suppliers = new ArrayList<>();
        markDto.getModels().forEach((m) -> suppliers.add(m::getName));

        return helper.createKeyboard(
                "Выбери модель",
                parser.getChatId(context),
                suppliers
        );
    }
}

