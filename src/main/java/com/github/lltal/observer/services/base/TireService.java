package com.github.lltal.observer.services.base;

import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.AbstractResolver;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.TireDto;
import com.github.lltal.observer.services.input.TireMarkInputService;
import com.github.lltal.observer.services.input.TireModelInputService;
import com.github.lltal.observer.services.input.TireTypeSizeInputService;
import com.github.lltal.observer.services.parser.ContextParser;
import com.github.lltal.observer.services.ui.UiHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import static com.github.lltal.observer.input.constant.TireConstants.TIRE_FILLER_NAME;
import static com.github.lltal.observer.input.constant.TireConstants.TIRE_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.TireConstants.TIRE_SENDER_NAME;

@Service
public class TireService {
    @Autowired
    @Qualifier(TIRE_SENDER_NAME)
    private AbstractSender sender;
    @Autowired
    @Qualifier(TIRE_RESOLVER_NAME)
    private AbstractResolver resolver;
    @Autowired
    @Qualifier(TIRE_FILLER_NAME)
    private AbstractFiller filler;
    @Autowired
    private ContextParser parser;
    @Autowired
    private TireMarkInputService markInputService;
    @Autowired
    private TireModelInputService modelInputService;
    @Autowired
    private TireTypeSizeInputService typeSizeInputService;
    @Autowired
    private UiHelper helper;

    public BotApiMethod<?> getNextMessage(TireDto tireDto, CommandContext context) {
        if (tireDto.getCount() == 0) {
            return sender.getNextMessage(tireDto, parser.getChatId(context));
        } else if (tireDto.getCount() == 1) {
            return markInputService.createMarksKeyboard(context);
        } else if (tireDto.getCount() == 2) {
            return sender.getNextMessage(tireDto, parser.getChatId(context));
        } else if (tireDto.getCount() == 3) {
            return modelInputService.createModelSelectionMessage(context, tireDto.getMarkName(), tireDto.getSeason());
        } else if (tireDto.getCount() == 4) {
            return typeSizeInputService.createTypeSizeKeyboard(context);
        } else if (tireDto.getCount() == 5) {
            return sender.getNextMessage(tireDto, parser.getChatId(context));
        }

        return sender.getNextMessage(tireDto, parser.getChatId(context));
    }

    public boolean fillDto(TireDto tireDto, CommandContext context) {
        if (tireDto.getCount() == 0) {
            filler.fill(tireDto, context.getName());
        } else if (tireDto.getCount() == 1) {
            filler.fill(tireDto, context.getName());
        } else if (tireDto.getCount() == 2) {
            resolver.resolve(tireDto, context);
        } else if (tireDto.getCount() == 3) {
            filler.fill(tireDto, context.getName());
        } else if (tireDto.getCount() == 4) {
            filler.fill(tireDto, context.getName());
        } else if (tireDto.getCount() == 5) {
            resolver.resolve(tireDto, context);
        }
        return tireDto.getCount() >= 6;
    }
}
