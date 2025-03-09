package com.github.lltal.observer.input.handler;

import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.TireMarkDto;
import com.github.lltal.observer.services.model.TireMarkService;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.github.lltal.observer.input.constant.TireMarkConstants.TIRE_MARK_FILLER_NAME;

@Component(TireMarkNameHandler.HANDLER_BEAN_NAME)
public class TireMarkNameHandler implements CustomFilleeHandler<TireMarkDto> {
    public static final String HANDLER_BEAN_NAME = "tireMarkNameHandler";
    @Autowired
    private TireMarkService markService;
    @Autowired
    private ContextParser contextParser;
    @Qualifier(TIRE_MARK_FILLER_NAME)
    @Autowired
    private AbstractFiller filler;

    @Override
    public void handleField(TireMarkDto dto, CommandContext context) {
        filler.fill(dto, context.getName());
        markService.create(dto);
    }
}
