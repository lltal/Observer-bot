package com.github.lltal.observer.input.enumeration;

import com.github.lltal.converter.shared.annotation.Convertee;
import com.github.lltal.converter.shared.annotation.ConverteeField;
import com.github.lltal.observer.config.constant.EnumConverterName;
import com.github.lltal.observer.config.constant.EnumStringView;


@Convertee(converterBeanName = EnumConverterName.ADMIN_ACTION_OBJECT_TYPE_CONVERTER_NAME)
public enum AdminActionObjectType {
    @ConverteeField(stringView = EnumStringView.USER_ID)
    USER_ID,
    @ConverteeField(stringView = EnumStringView.LOCATION)
    LOCATION,
    @ConverteeField(stringView = EnumStringView.MARK)
    MARK,
    @ConverteeField(stringView = EnumStringView.MODEL)
    MODEL,
    @ConverteeField(stringView = EnumStringView.TYPE_SIZE)
    TYPE_SIZE

}
