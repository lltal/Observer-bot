package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.annotation.FilleeField;
import com.github.lltal.filler.shared.ifc.Countable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TireModelDto implements Countable {
    private String name;
    private int code;
    private TireMarkDto markDto;
    private int count;

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }
}
