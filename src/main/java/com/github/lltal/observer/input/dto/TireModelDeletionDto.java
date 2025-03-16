package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Button;
import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.annotation.Keyboard;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.input.enumeration.Season;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.github.lltal.observer.config.constant.EnumStringView.SUMMER;
import static com.github.lltal.observer.config.constant.EnumStringView.WINTER;
import static com.github.lltal.observer.config.constant.SenderName.TIRE_MODEL_DELETION_SENDER_NAME;

@Fillee(
        senderBeanName = TIRE_MODEL_DELETION_SENDER_NAME
)
@Data
@NoArgsConstructor
public class TireModelDeletionDto implements Countable {
    @FilleeField
    private String markName;
    @Keyboard(
            buttons = {
                    @Button(userView = SUMMER, cbValue = SUMMER),
                    @Button(userView = WINTER, cbValue = WINTER)
            }
    )
    @FilleeField(text = "Выбери сезон")
    private Season season;
    @FilleeField
    private String name;

    @FilleeField(text = "Модель успешно удалена")
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
