package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.input.enumeration.AdminActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

import static com.github.lltal.observer.input.constant.TireMarkConstants.TIRE_MARK_FILLER_NAME;
import static com.github.lltal.observer.input.constant.TireMarkConstants.TIRE_MARK_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.TireMarkConstants.TIRE_MARK_SENDER_NAME;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Fillee(
        senderBeanName = TIRE_MARK_SENDER_NAME,
        fillerBeanName = TIRE_MARK_FILLER_NAME,
        resolverBeanName = TIRE_MARK_RESOLVER_NAME
)
public class TireMarkDto implements Countable {
    private AdminActionType actionType;
    @FilleeField(text = "Введи название марки")
    private String name;
    private Collection<TireModelDto> models = new ArrayList<>();
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
