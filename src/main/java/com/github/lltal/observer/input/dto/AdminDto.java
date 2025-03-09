package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Button;
import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.annotation.Keyboard;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.input.enumeration.AdminActionObjectType;
import com.github.lltal.observer.input.enumeration.AdminActionType;
import com.github.lltal.observer.input.enumeration.YesNo;
import com.github.lltal.observer.input.handler.AdminActionObjectTypeHandler;
import com.github.lltal.observer.input.handler.AdminActionTypeHandler;
import com.github.lltal.observer.input.handler.AdminYesNoHandler;
import lombok.Data;

import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_FILLER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_RESOLVER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_SENDER_NAME;
import static com.github.lltal.observer.input.constant.AdminConstants.ADD;
import static com.github.lltal.observer.input.constant.AdminConstants.LOCATION;
import static com.github.lltal.observer.input.constant.AdminConstants.MARK;
import static com.github.lltal.observer.input.constant.AdminConstants.MODEL;
import static com.github.lltal.observer.input.constant.AdminConstants.REMOVE;
import static com.github.lltal.observer.input.constant.AdminConstants.TYPE_SIZE;
import static com.github.lltal.observer.input.constant.AdminConstants.USER_ID;
import static com.github.lltal.observer.input.constant.CommonConstants.NO;
import static com.github.lltal.observer.input.constant.CommonConstants.YES;

@Data
@Fillee(
        senderBeanName = ACTION_SENDER_NAME,
        fillerBeanName = ACTION_FILLER_NAME,
        resolverBeanName = ACTION_RESOLVER_NAME
)
public class AdminDto implements Countable {
     @Keyboard(
             buttons = {
                     @Button(userView = YES, cbValue = YES),
                     @Button(userView = NO, cbValue = NO)
             }
     )
     @FilleeField(text = "Следующее действие?", customFillHandler = AdminYesNoHandler.HANDLER_BEAN_NAME)
     private YesNo yesNo;

     @Keyboard(buttons = {
             @Button(userView = USER_ID, cbValue = USER_ID),
             @Button(userView = LOCATION, cbValue = LOCATION),
             @Button(userView = TYPE_SIZE, cbValue = TYPE_SIZE),
             @Button(userView = MARK, cbValue = MARK),
             @Button(userView = MODEL, cbValue = MODEL)
     })
     @FilleeField(text = "С чем произвести действие?", customFillHandler = AdminActionObjectTypeHandler.HANDLER_BEAN_NAME)
     private AdminActionObjectType objectType;

     @Keyboard(buttons = {
             @Button(userView = ADD, cbValue = ADD),
             @Button(userView = REMOVE, cbValue = REMOVE)
     })
     @FilleeField(text = "Какое действие выполнить?", customFillHandler = AdminActionTypeHandler.HANDLER_BEAN_NAME)
     private AdminActionType actionType;

     @FilleeField
     private Countable newValue;

     private int fillCount;

     @Override
     public int getCount() {
          return fillCount;
     }

     @Override
     public void setCount(int count) {
          fillCount = count;
     }
}
