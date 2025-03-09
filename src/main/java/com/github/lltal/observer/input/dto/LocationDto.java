package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.input.handler.LocationStreetHandler;
import lombok.Builder;
import lombok.Data;

import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_FILLER_NAME;
import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.LocationConstants.LOCATION_SENDER_NAME;

@Data
@Builder
@Fillee(
        senderBeanName = LOCATION_SENDER_NAME,
        fillerBeanName = LOCATION_FILLER_NAME,
        resolverBeanName = LOCATION_RESOLVER_NAME
)
public class LocationDto implements Countable {
    @FilleeField(text = "Введи улицу", customFillHandler = LocationStreetHandler.HANDLER_BEAN_NAME)
    private String street;
    @FilleeField(text = "Адрес успешно создан")
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
