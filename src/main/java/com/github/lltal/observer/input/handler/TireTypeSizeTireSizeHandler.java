package com.github.lltal.observer.input.handler;

import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.TireTypeSizeDto;
import com.github.lltal.observer.services.model.TireTypeSizeService;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.github.lltal.observer.input.constant.TireTypeSizeConstants.TIRE_TYPE_SIZE_FILLER_NAME;

@Component(TireTypeSizeTireSizeHandler.HANDLER_BEAN_NAME)
public class TireTypeSizeTireSizeHandler implements CustomFilleeHandler<TireTypeSizeDto> {
    public static final String HANDLER_BEAN_NAME = "tireTypeSizeTireSizeHandler";
    @Autowired
    private TireTypeSizeService tireTypeSizeService;
    @Autowired
    private ContextParser contextParser;
    @Qualifier(TIRE_TYPE_SIZE_FILLER_NAME)
    @Autowired
    private AbstractFiller filler;

    @Override
    public void handleField(TireTypeSizeDto dto, CommandContext context) {
        filler.fill(dto, context.getName());
        tireTypeSizeService.create(dto);
    }
}
