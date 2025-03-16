package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Button;
import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.annotation.Keyboard;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.input.enumeration.YesNo;
import com.github.lltal.observer.service.front.ui.UiHelper;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.github.lltal.observer.config.constant.EnumStringView.NO;
import static com.github.lltal.observer.config.constant.EnumStringView.YES;
import static com.github.lltal.observer.config.constant.SenderName.DUTY_SENDER_NAME;

@Data
@Fillee(
        senderBeanName = DUTY_SENDER_NAME
)
@Valid
public class DutyDto implements Countable {
    private final Instant createdAt = Instant.now();

    @FilleeField(text = "Введи фио для запуска смены")
    private String fio;
    @FilleeField
    private String tgId;
    @FilleeField(text = "Введи номер телефона в формате: +70001234567")
    private String phoneNumber;
    @Keyboard(
            buttons = {
                    @Button(userView = YES, cbValue = YES),
                    @Button(userView = NO, cbValue = NO)
            }
    )
    @FilleeField(text = "Следующее колесо?")
    private YesNo yesNo;
    @FilleeField(text = "Новое колесо")
    private List<TireDto> tires = new ArrayList<>();

    private int count;

    @Override
    public int getCount() {
        return count;
    }

    public List<TireDto> getTires() {
        return tires;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }
}
