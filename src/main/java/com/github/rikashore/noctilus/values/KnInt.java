package com.github.rikashore.noctilus.values;

public class KnInt extends KnValue {
    public final Integer value;

    public KnInt(Integer value) {
        this.value = value;
    }

    @Override
    public String getDebugRepresentation() {
        return this.value.toString();
    }
}
