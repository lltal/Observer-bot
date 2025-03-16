package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Button;
import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.annotation.Keyboard;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.input.enumeration.Season;
import com.github.lltal.observer.input.enumeration.YesNo;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.Instant;

import static com.github.lltal.observer.config.constant.EnumStringView.NO;
import static com.github.lltal.observer.config.constant.EnumStringView.SUMMER;
import static com.github.lltal.observer.config.constant.EnumStringView.WINTER;
import static com.github.lltal.observer.config.constant.EnumStringView.YES;
import static com.github.lltal.observer.config.constant.SenderName.TIRE_SENDER_NAME;

@Data
@Fillee(
        senderBeanName = TIRE_SENDER_NAME
)
@Valid
public class TireDto implements Countable {
    private final Instant createdAt = Instant.now();

    @FilleeField(text = "Введи номер авто в формате: х111хх797")
    private String carNumber;
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
    private String modelName;
    @FilleeField
    private String typeSize;
    @Keyboard(
            buttons = {
                    @Button(userView = YES, cbValue = YES),
                    @Button(userView = NO, cbValue = NO)
            }
    )
    @FilleeField(text = "Колесо введено успешно?")
    private YesNo createSuccessfully;
    @FilleeField(text = "Колесо успешно создано")
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
