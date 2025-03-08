package com.github.lltal.observer.input.enumeration;

import com.github.lltal.converter.shared.annotation.Convertee;
import com.github.lltal.converter.shared.annotation.ConverteeField;
import com.github.lltal.observer.input.constant.AdminConstants;

import static com.github.lltal.observer.input.constant.AdminConstants.ACTION_TYPE_CONVERTER_NAME;

@Convertee(converterBeanName = ACTION_TYPE_CONVERTER_NAME)
public enum AdminActionType {
    @ConverteeField(stringView = AdminConstants.ADD)
    ADD,
    @ConverteeField(stringView = AdminConstants.REMOVE)
    REMOVE
}
