package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.ifc.Countable;
import lombok.Data;

import static com.github.lltal.observer.config.constant.SenderName.TIRE_TYPE_SIZE_DELETION_SENDER_NAME;

@Data
@Fillee(
        senderBeanName = TIRE_TYPE_SIZE_DELETION_SENDER_NAME
)
public class TireTypeSizeDeletionDto implements Countable {
    @FilleeField
    private String tireSize;
    @FilleeField(text = "Типоразмер успешно удален")
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
