package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.ifc.Countable;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.github.lltal.observer.config.constant.SenderName.LOCATION_DELETION_SENDER_NAME;

@Data
@Fillee(
        senderBeanName = LOCATION_DELETION_SENDER_NAME
)
@NoArgsConstructor
public class LocationDeletionDto implements Countable {
    @FilleeField
    private String street;
    @FilleeField(text = "Адрес успешно удален")
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
