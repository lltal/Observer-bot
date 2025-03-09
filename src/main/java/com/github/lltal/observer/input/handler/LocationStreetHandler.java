package com.github.lltal.observer.input.handler;

import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.LocationDto;
import com.github.lltal.observer.input.exception.DuplicateValueException;
import com.github.lltal.observer.services.model.LocationService;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_FILLER_NAME;

@Component(LocationStreetHandler.HANDLER_BEAN_NAME)
public class LocationStreetHandler implements CustomFilleeHandler<LocationDto> {
    public static final String HANDLER_BEAN_NAME = "locationStreetHandler";

    @Qualifier(LOCATION_FILLER_NAME)
    @Autowired
    private AbstractFiller filler;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ContextParser parser;

    @Override
    public void handleField(LocationDto dto, CommandContext context) {
        filler.fill(dto, context.getName());
        if (locationService.contains(dto.getStreet()))
            throw new DuplicateValueException(String.format("Улица %s уже существует", context.getName()));
        locationService.create(dto);
    }
}
