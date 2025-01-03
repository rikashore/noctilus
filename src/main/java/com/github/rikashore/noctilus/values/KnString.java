package com.github.rikashore.noctilus.values;

public class KnString extends KnValue {
    public final String value;

    public KnString(String value) {
        this.value = value;
    }

    @Override
    String getDebugRepresentation() {
        return "\"" + this.value + "\"";
    }
}
