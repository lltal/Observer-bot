package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.ifc.Countable;
import lombok.Data;

import static com.github.lltal.observer.config.constant.SenderName.USER_SENDER_NAME;

@Data
@Fillee(
        senderBeanName = USER_SENDER_NAME
)
public class UserDto implements Countable {
    @FilleeField(text = "Введи id пользователя")
    private String tgId;
    @FilleeField(text = "Пользователь успешно создан")
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
