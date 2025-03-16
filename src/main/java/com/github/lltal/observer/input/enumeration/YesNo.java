package com.github.lltal.observer.input.enumeration;

import com.github.lltal.converter.shared.annotation.Convertee;
import com.github.lltal.converter.shared.annotation.ConverteeField;
import com.github.lltal.observer.config.constant.EnumStringView;

import static com.github.lltal.observer.config.constant.EnumConverterName.YES_NO_CONVERTER_NAME;

@Convertee(converterBeanName = YES_NO_CONVERTER_NAME)
public enum YesNo {
    @ConverteeField(stringView = EnumStringView.YES)
    YES,
    @ConverteeField(stringView = EnumStringView.NO)
    NO,
    @ConverteeField(stringView = EnumStringView.CLOSE)
    CLOSE
}
