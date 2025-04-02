package com.github.lltal.observer.config.constant.enumeration.converter;

import com.github.lltal.converter.shared.ifc.AbstractConverter;
import com.github.lltal.observer.config.constant.EnumConverterName;
import com.github.lltal.observer.config.constant.enumeration.AdminActionObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AdminActionObjectTypeConverter {
    @Qualifier(EnumConverterName.ADMIN_ACTION_OBJECT_TYPE_CONVERTER_NAME)
    @Autowired
    private AbstractConverter<AdminActionObjectType> converter;

    public AdminActionObjectType convertToEnum(String value) {
        return converter.convertToEnumValue(value);
    }

    public String convertToString(AdminActionObjectType value) {
        return converter.convertToStringView(value);
    }
}
