package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Button;
import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.annotation.Keyboard;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.input.enumeration.YesNo;
import com.github.lltal.observer.input.handler.AdminYesNoHandler;
import lombok.Data;

import java.time.Instant;

import static com.github.lltal.observer.input.constant.CommonConstants.NO;
import static com.github.lltal.observer.input.constant.CommonConstants.YES;
import static com.github.lltal.observer.input.constant.DutyConstants.DUTY_FILLER_NAME;
import static com.github.lltal.observer.input.constant.DutyConstants.DUTY_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.DutyConstants.DUTY_SENDER_NAME;


@Data
@Fillee(
        senderBeanName = DUTY_SENDER_NAME,
        fillerBeanName = DUTY_FILLER_NAME,
        resolverBeanName = DUTY_RESOLVER_NAME
)
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
    @FilleeField(text = "Следующее колесо?", customFillHandler = AdminYesNoHandler.HANDLER_BEAN_NAME)
    private YesNo yesNo;
    @FilleeField(text = "Новое колосо")
    private TireDto tire;

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
