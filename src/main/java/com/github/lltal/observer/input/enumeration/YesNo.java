package com.github.lltal.observer.input.enumeration;

import com.github.lltal.converter.shared.annotation.Convertee;
import com.github.lltal.converter.shared.annotation.ConverteeField;
import com.github.lltal.observer.input.constant.CommonConstants;

import static com.github.lltal.observer.input.constant.CommonConstants.YES_NO_CONVERTER_NAME;

@Convertee(converterBeanName = YES_NO_CONVERTER_NAME)
public enum YesNo {
    @ConverteeField(stringView = CommonConstants.YES)
    YES,
    @ConverteeField(stringView = CommonConstants.NO)
    NO
}
