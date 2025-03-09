package com.github.lltal.observer.input.handler;

import com.github.lltal.filler.shared.ifc.AbstractFiller;
import com.github.lltal.filler.shared.ifc.AbstractSender;
import com.github.lltal.filler.shared.ifc.CustomFilleeHandler;
import com.github.lltal.filler.starter.command.CommandContext;
import com.github.lltal.observer.input.dto.TireModelDto;
import com.github.lltal.observer.services.model.TireModelService;
import com.github.lltal.observer.services.parser.ContextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.github.lltal.observer.input.constant.TireModelConstants.TIRE_MODEL_FILLER_NAME;
import static com.github.lltal.observer.input.constant.TireModelConstants.TIRE_MODEL_SENDER_NAME;

@Component(TireModelCodeHandler.HANDLER_BEAN_NAME)
public class TireModelCodeHandler implements CustomFilleeHandler<TireModelDto> {
    public static final String HANDLER_BEAN_NAME = "tireModelCodeHandler";

    @Autowired
    private TireModelService modelService;
    @Autowired
    private ContextParser contextParser;
    @Qualifier(TIRE_MODEL_FILLER_NAME)
    @Autowired
    private AbstractFiller filler;

    @Override
    public void handleField(TireModelDto dto, CommandContext context) {
        filler.fill(dto, Integer.parseInt(context.getName()));
        modelService.create(dto);
    }
}
