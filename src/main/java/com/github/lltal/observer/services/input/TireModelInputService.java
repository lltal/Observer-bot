package com.github.lltal.observer.services.input;

import com.github.lltal.converter.shared.ifc.AbstractConverter;
import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.constant.CommonConstants;
import com.github.lltal.observer.input.dto.TireMarkDto;
import com.github.lltal.observer.input.dto.TireModelDto;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.enumeration.Season;
import com.github.lltal.observer.input.exception.EmptyListException;
import com.github.lltal.observer.services.base.InputService;
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

import static com.github.lltal.observer.input.constant.TireModelConstants.TIRE_MODEL_FILLER_NAME;
import static com.github.lltal.observer.input.constant.TireModelConstants.TIRE_MODEL_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.TireModelConstants.TIRE_MODEL_SENDER_NAME;

@Service
public class TireModelInputService implements InputService {
    @Autowired
    private TireModelBuilder builder;
    @Autowired
    @Qualifier(TIRE_MODEL_SENDER_NAME)
    private AbstractSender sender;
    @Autowired
    @Qualifier(TIRE_MODEL_FILLER_NAME)
    private AbstractFiller filler;
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
    @Autowired
    @Qualifier(CommonConstants.SEASON_CONVERTER_NAME)
    private AbstractConverter<Season> converter;
    @Autowired
    private TireMarkInputService markInputService;

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
            return markInputService.createMarksKeyboard(context);
        } else if (manageableDto.getCount() == 1) {
            helper.closeCb(context);
        }

        return sender.getNextMessage(manageableDto, parser.getChatId(context));
    }

    @Override
    public BotApiMethod<?> getNextDeletionMessage(Countable manageableDto, CommandContext context) {
        if (manageableDto.getCount() == 0) {
            return markInputService.createMarksKeyboard(context);
        }
        if (manageableDto.getCount() == 1) {
            sender.closeCb(context);
            return sender.getNextMessage(manageableDto, parser.getChatId(context));
        } else if (manageableDto.getCount() == 2) {
            sender.closeCb(context);
            TireModelDto modelDto = (TireModelDto) manageableDto;
            return createModelSelectionMessage(context, modelDto.getMarkDto().getName(), modelDto.getSeason());
        }

        return SendMessage.builder()
                .text("Модель успешно удалена")
                .chatId(parser.getChatId(context))
                .build();
    }

    @Override
    public boolean fillDto(Countable manageableDto, CommandContext context) {
        if (manageableDto.getCount() == 0) {
           setMark(manageableDto, context);
        }
        else
            resolver.resolve(manageableDto, context);

        return manageableDto.getCount() >= 4;
    }

    @Override
    public boolean deleteIfCan(Countable manageableDto, CommandContext context) {
        if (manageableDto.getCount() == 0) {
            setMark(manageableDto, context);
        } else if (manageableDto.getCount() == 1) {
            filler.fill(manageableDto, converter.convertToEnumValue(context.getName()));
        } else if (manageableDto.getCount() == 2) {
            filler.fill(manageableDto, context.getName());
            modelService.delete(
                    (TireModelDto) manageableDto
            );
        }
        return manageableDto.getCount() >= 3;
    }

    public BotApiMethod<?> createModelSelectionMessage(CommandContext context, String markName, Season season) {
        Collection<String> names = modelService.findAllNames(markName, season);
        Collection<Supplier<String>> suppliers = new ArrayList<>();
        names.forEach((n) -> suppliers.add(() -> n));

        if (names.isEmpty())
            throw new EmptyListException("Список моделей пуст");

        return helper.createKeyboard(
                "Выбери модель",
                parser.getChatId(context),
                suppliers
        );
    }

    private void setMark(Countable manageableDto, CommandContext context) {
        TireMarkDto markDto = markService.find(context.getName());
        ((TireModelDto)manageableDto).setMarkDto(markDto);
        manageableDto.setCount(manageableDto.getCount() + 1);
    }
}

