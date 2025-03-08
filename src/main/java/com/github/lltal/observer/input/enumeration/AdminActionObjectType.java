package com.github.lltal.observer.input.enumeration;

import com.github.lltal.converter.shared.annotation.Convertee;
import com.github.lltal.converter.shared.annotation.ConverteeField;
import com.github.lltal.observer.input.constant.AdminConstants;

import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_OBJECT_TYPE_CONVERTER_NAME;

@Convertee(converterBeanName = ACTION_OBJECT_TYPE_CONVERTER_NAME)
public enum AdminActionObjectType {
    @ConverteeField(stringView = AdminConstants.USER_ID)
    USER_ID,
    @ConverteeField(stringView = AdminConstants.LOCATION)
    LOCATION,
    @ConverteeField(stringView = AdminConstants.MARK)
    MARK,
    @ConverteeField(stringView = AdminConstants.MODEL)
    MODEL,
    @ConverteeField(stringView = AdminConstants.TYPE_SIZE)
    TYPE_SIZE
}
