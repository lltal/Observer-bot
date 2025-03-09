package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.input.handler.UserTgIdHandler;
import lombok.Builder;
import lombok.Data;

import static com.github.lltal.observer.input.constant.UserConstants.USER_FILLER_NAME;
import static com.github.lltal.observer.input.constant.UserConstants.USER_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.UserConstants.USER_SENDER_NAME;

@Data
@Builder
@Fillee(
        senderBeanName = USER_SENDER_NAME,
        fillerBeanName = USER_FILLER_NAME,
        resolverBeanName = USER_RESOLVER_NAME
)
public class UserDto implements Countable {
    @FilleeField(text = "Введи id пользователя", customFillHandler = UserTgIdHandler.HANDLER_BEAN_NAME)
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
