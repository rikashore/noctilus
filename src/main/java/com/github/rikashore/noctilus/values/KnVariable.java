package com.github.rikashore.noctilus.values;

public class KnVariable extends KnValue {
    public final String name;

    public KnVariable(String name) {
        this.name = name;
    }

    @Override
    public String getDebugRepresentation() {
        return String.format("ident(%s)", this.name);
    }
}
