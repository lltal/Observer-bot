package com.github.lltal.observer.input.enumeration;

import com.github.lltal.converter.shared.annotation.Convertee;
import com.github.lltal.converter.shared.annotation.ConverteeField;
import com.github.lltal.observer.input.constant.CommonConstants;

import static com.github.lltal.observer.input.constant.CommonConstants.SEASON_CONVERTER_NAME;

@Convertee(converterBeanName = SEASON_CONVERTER_NAME)
public enum Season {
    @ConverteeField(stringView = CommonConstants.SUMMER)
    SUMMER,
    @ConverteeField(stringView = CommonConstants.WINTER)
    WINTER
}
