package com.github.lltal.observer.input.enumeration;

import com.github.lltal.converter.shared.annotation.Convertee;
import com.github.lltal.converter.shared.annotation.ConverteeField;
import com.github.lltal.observer.config.constant.EnumStringView;

import static com.github.lltal.observer.config.constant.EnumConverterName.SEASON_CONVERTER_NAME;

@Convertee(converterBeanName = SEASON_CONVERTER_NAME)
public enum Season {
    @ConverteeField(stringView = EnumStringView.SUMMER)
    SUMMER,
    @ConverteeField(stringView = EnumStringView.WINTER)
    WINTER
}
