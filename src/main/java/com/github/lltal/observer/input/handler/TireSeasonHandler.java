package com.github.lltal.observer.input.handler;

import com.github.lltal.converter.shared.ifc.AbstractConverter;
import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.TireDto;
import com.github.lltal.observer.input.enumeration.Season;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.github.lltal.observer.input.constant.CommonConstants.SEASON_CONVERTER_NAME;
import static com.github.lltal.observer.input.constant.TireConstants.TIRE_FILLER_NAME;
import static com.github.lltal.observer.input.constant.TireConstants.TIRE_SENDER_NAME;

@Component(TireSeasonHandler.HANDLER_BEAN_NAME)
public class TireSeasonHandler implements CustomFilleeHandler<TireDto> {
    public static final String HANDLER_BEAN_NAME = "tireSeasonHandler";

    @Qualifier(TIRE_FILLER_NAME)
    @Autowired
    private AbstractFiller filler;
    @Autowired
    @Qualifier(TIRE_SENDER_NAME)
    private AbstractSender sender;
    @Autowired
    @Qualifier(SEASON_CONVERTER_NAME)
    private AbstractConverter<Season> converter;
    @Autowired
    private ContextParser parser;

    @Override
    public void handleField(TireDto dto, CommandContext context) {
        context.getEngine().executeNotException(sender.closeCb(context));
        filler.fill(dto, converter.convertToEnumValue(context.getName()));
    }
}
