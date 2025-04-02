package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.Button;
import com.github.lltal.filler.shared.annotation.Fillee;
import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.annotation.Keyboard;
import com.github.lltal.filler.shared.ifc.Countable;
import com.github.lltal.observer.config.constant.enumeration.AdminActionObjectType;
import com.github.lltal.observer.config.constant.enumeration.AdminActionType;
import com.github.lltal.observer.config.constant.enumeration.YesNo;
import lombok.Data;

import static com.github.lltal.observer.config.constant.EnumStringView.ADD;
import static com.github.lltal.observer.config.constant.EnumStringView.LOCATION;
import static com.github.lltal.observer.config.constant.EnumStringView.MARK;
import static com.github.lltal.observer.config.constant.EnumStringView.MODEL;
import static com.github.lltal.observer.config.constant.EnumStringView.NO;
import static com.github.lltal.observer.config.constant.EnumStringView.REMOVE;
import static com.github.lltal.observer.config.constant.EnumStringView.TYPE_SIZE;
import static com.github.lltal.observer.config.constant.EnumStringView.USER_ID;
import static com.github.lltal.observer.config.constant.EnumStringView.YES;
import static com.github.lltal.observer.config.constant.SenderName.ADMIN_SENDER_NAME;

@Data
@Fillee(
        senderBeanName = ADMIN_SENDER_NAME
)
public class AdminDto implements Countable {
     @Keyboard(
             buttons = {
                     @Button(userView = YES, cbValue = YES),
                     @Button(userView = NO, cbValue = NO)
             }
     )
     @FilleeField(text = "Следующее действие?")
     private YesNo yesNo;

     @Keyboard(buttons = {
             @Button(userView = USER_ID, cbValue = USER_ID),
             @Button(userView = LOCATION, cbValue = LOCATION),
             @Button(userView = TYPE_SIZE, cbValue = TYPE_SIZE),
             @Button(userView = MARK, cbValue = MARK),
             @Button(userView = MODEL, cbValue = MODEL)
     })
     @FilleeField(text = "С чем произвести действие?")
     private AdminActionObjectType objectType;

     @Keyboard(buttons = {
             @Button(userView = ADD, cbValue = ADD),
             @Button(userView = REMOVE, cbValue = REMOVE)
     })
     @FilleeField(text = "Какое действие выполнить?")
     private AdminActionType actionType;

     @FilleeField
     private Countable newValue;

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
