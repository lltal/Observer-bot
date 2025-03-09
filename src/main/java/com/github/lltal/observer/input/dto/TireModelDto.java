package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.ifc.Countable;
import lombok.Builder;
import lombok.Data;

import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_FILLER_NAME;
import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_SENDER_NAME;
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
    @FilleeField(text = "Введи название модели")
    private String name;
    @FilleeField(text = "Введи код 1с")
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
