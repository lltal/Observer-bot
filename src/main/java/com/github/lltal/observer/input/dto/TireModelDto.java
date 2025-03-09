package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Button;
import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.annotation.Keyboard;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.input.constant.CommonConstants;
import com.github.lltal.observer.input.enumeration.Season;
import com.github.lltal.observer.input.handler.TireModelCodeHandler;
import com.github.lltal.observer.input.handler.TireModelSeasonHandler;
import lombok.Builder;
import lombok.Data;

import static com.github.lltal.observer.input.constant.TireModelConstants.TIRE_MODEL_FILLER_NAME;
import static com.github.lltal.observer.input.constant.TireModelConstants.TIRE_MODEL_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.TireModelConstants.TIRE_MODEL_SENDER_NAME;

@Data
@Builder
@Fillee(
        senderBeanName = TIRE_MODEL_SENDER_NAME,
        fillerBeanName = TIRE_MODEL_FILLER_NAME,
        resolverBeanName = TIRE_MODEL_RESOLVER_NAME
)
public class TireModelDto implements Countable {
    @FilleeField
    private TireMarkDto markDto;
    @Keyboard(
            buttons = {
                    @Button(userView = CommonConstants.SUMMER, cbValue = CommonConstants.SUMMER),
                    @Button(userView = CommonConstants.WINTER, cbValue = CommonConstants.WINTER)
            }
    )
    @FilleeField(text = "Выбери сезон", customFillHandler = TireModelSeasonHandler.HANDLER_BEAN_NAME)
    private Season season;
    @FilleeField(text = "Введи название модели")
    private String name;
    @FilleeField(text = "Введи код 1с", customFillHandler = TireModelCodeHandler.HANDLER_BEAN_NAME)
    private int code;
    @FilleeField(text = "Модель успешно создана")
    private String finalMessage;
    private int count;

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }
}
