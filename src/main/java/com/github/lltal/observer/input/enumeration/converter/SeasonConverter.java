package com.github.lltal.observer.input.enumeration.converter;

import com.github.lltal.converter.shared.ifc.AbstractConverter;
import com.github.lltal.observer.config.constant.EnumConverterName;
import com.github.lltal.observer.input.enumeration.Season;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SeasonConverter {
    @Qualifier(EnumConverterName.SEASON_CONVERTER_NAME)
    @Autowired
    private AbstractConverter<Season> converter;

    public Season convertToEnum(String value) {
        return converter.convertToEnumValue(value);
    }

    public String convertToString(Season value) {
        return converter.convertToStringView(value);
    }
}
