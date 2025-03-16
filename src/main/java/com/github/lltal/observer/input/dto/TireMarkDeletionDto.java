package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.ifc.Countable;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.github.lltal.observer.config.constant.SenderName.TIRE_MARK_DELETION_SENDER_NAME;

@Data
@NoArgsConstructor
@Fillee(
        senderBeanName = TIRE_MARK_DELETION_SENDER_NAME
)
public class TireMarkDeletionDto implements Countable {
    @FilleeField
    private String name;
    @FilleeField(text = "Марка успешно удалена")
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
