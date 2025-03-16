package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.ifc.Countable;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.github.lltal.observer.config.constant.SenderName.LOCATION_SENDER_NAME;

@Data
@Fillee(
        senderBeanName = LOCATION_SENDER_NAME
)
@NoArgsConstructor
public class LocationDto implements Countable {
    @FilleeField(text = "Введи улицу")
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
