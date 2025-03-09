package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Button;
import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.annotation.Keyboard;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.input.constant.CommonConstants;
import com.github.lltal.observer.input.enumeration.Season;
import com.github.lltal.observer.input.enumeration.YesNo;
import com.github.lltal.observer.input.handler.AdminYesNoHandler;
import com.github.lltal.observer.input.handler.TireSeasonHandler;
import lombok.Data;

import java.time.Instant;

import static com.github.lltal.observer.input.constant.CommonConstants.CLOSE;
import static com.github.lltal.observer.input.constant.CommonConstants.NO;
import static com.github.lltal.observer.input.constant.CommonConstants.YES;
import static com.github.lltal.observer.input.constant.TireConstants.TIRE_FILLER_NAME;
import static com.github.lltal.observer.input.constant.TireConstants.TIRE_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.TireConstants.TIRE_SENDER_NAME;

@Data
@Fillee(
        senderBeanName = TIRE_SENDER_NAME,
        fillerBeanName = TIRE_FILLER_NAME,
        resolverBeanName = TIRE_RESOLVER_NAME
)
public class TireDto implements Countable {
    private final Instant createdAt = Instant.now();

    @FilleeField(text = "Введи номер авто в формате: х111хх797")
    private String carNumber;
    @FilleeField
    private String markName;
    @Keyboard(
            buttons = {
                    @Button(userView = CommonConstants.SUMMER, cbValue = CommonConstants.SUMMER),
                    @Button(userView = CommonConstants.WINTER, cbValue = CommonConstants.WINTER)
            }
    )
    @FilleeField(text = "Выбери сезон", customFillHandler = TireSeasonHandler.HANDLER_BEAN_NAME)
    private Season season;
    @FilleeField
    private String modelName;
    @FilleeField
    private String typeSize;
    @Keyboard(
            buttons = {
                    @Button(userView = YES, cbValue = YES),
                    @Button(userView = NO, cbValue = NO),
                    @Button(userView = CLOSE, cbValue = CLOSE)
            }
    )
    @FilleeField(text = "Колесо введено успешно?", customFillHandler = AdminYesNoHandler.HANDLER_BEAN_NAME)
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
