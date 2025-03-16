package com.github.lltal.observer.input.enumeration.converter;

import com.github.lltal.converter.shared.ifc.AbstractConverter;
import com.github.lltal.observer.input.enumeration.YesNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.github.lltal.observer.config.constant.EnumConverterName.YES_NO_CONVERTER_NAME;

@Component
public class YesNoConverter {
    @Qualifier(YES_NO_CONVERTER_NAME)
    @Autowired
    private AbstractConverter<YesNo> converter;

    public YesNo convertToEnum(String value) {
        return converter.convertToEnumValue(value);
    }

    public String convertToString(YesNo value) {
        return converter.convertToStringView(value);
    }
}
