package com.github.rikashore.noctilus.values;

public class KnBlock extends KnValue {
    public final KnValue value;

    public KnBlock(KnValue value) {
        this.value = value;
    }

    @Override
    String getDebugRepresentation() {
        return "BLOCK object " + this.value.getDebugRepresentation();
    }
}
