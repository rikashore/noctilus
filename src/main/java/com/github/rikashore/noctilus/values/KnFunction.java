package com.github.rikashore.noctilus.values;

public class KnFunction extends KnValue {
    public final String name;
    public final KnValue[] args;

    public KnFunction(String name, KnValue[] args) {
        this.name = name;
        this.args = args;
    }
}
