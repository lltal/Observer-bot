package com.github.lltal.observer.input.dto;

import com.github.lltal.filler.shared.ifc.Countable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto implements Countable {
    private final String tgId;
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
