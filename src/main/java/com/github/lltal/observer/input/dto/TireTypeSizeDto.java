package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.input.handler.TireTypeSizeTireSizeHandler;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static com.github.lltal.observer.input.constant.TireTypeSizeConstants.TIRE_TYPE_SIZE_FILLER_NAME;
import static com.github.lltal.observer.input.constant.TireTypeSizeConstants.TIRE_TYPE_SIZE_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.TireTypeSizeConstants.TIRE_TYPE_SIZE_SENDER_NAME;

@Data
@Builder
@Fillee(
        senderBeanName = TIRE_TYPE_SIZE_SENDER_NAME,
        fillerBeanName = TIRE_TYPE_SIZE_FILLER_NAME,
        resolverBeanName = TIRE_TYPE_SIZE_RESOLVER_NAME
)
@Valid
public class TireTypeSizeDto implements Countable {

    @Pattern(regexp = "^\\d+/(\\d+)/(\\d+)$")
    @FilleeField(text = "Введи типоразмер", customFillHandler = TireTypeSizeTireSizeHandler.HANDLER_BEAN_NAME)
    private String tireSize;
    @FilleeField(text = "Типоразмер успешно создан")
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
