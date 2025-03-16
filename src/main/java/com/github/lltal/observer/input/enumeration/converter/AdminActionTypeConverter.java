package com.github.lltal.observer.input.enumeration.converter;

import com.github.lltal.converter.shared.ifc.AbstractConverter;
import com.github.lltal.observer.config.constant.EnumConverterName;
import com.github.lltal.observer.input.enumeration.AdminActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AdminActionTypeConverter {
    @Qualifier(EnumConverterName.ADMIN_ACTION_TYPE_CONVERTER_NAME)
    @Autowired
    private AbstractConverter<AdminActionType> converter;

    public AdminActionType convertToEnum(String value) {
        return converter.convertToEnumValue(value);
    }

    public String convertToString(AdminActionType value) {
        return converter.convertToStringView(value);
    }
}
