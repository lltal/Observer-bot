package com.github.lltal.observer.config.constant.enumeration;

import com.github.lltal.converter.shared.annotation.Convertee;
import com.github.lltal.converter.shared.annotation.ConverteeField;
import com.github.lltal.observer.config.constant.EnumConverterName;
import com.github.lltal.observer.config.constant.EnumStringView;

@Convertee(converterBeanName = EnumConverterName.ADMIN_ACTION_TYPE_CONVERTER_NAME)
public enum AdminActionType {
    @ConverteeField(stringView = EnumStringView.ADD)
    ADD,
    @ConverteeField(stringView = EnumStringView.REMOVE)
    REMOVE
}
