package com.github.rikashore.noctilus.values;

import java.util.Arrays;

public class KnList extends KnValue {
    public final KnValue[] backing;

    public KnList(KnValue[] backing) {
        this.backing = backing;
    }

    @Override
    String getDebugRepresentation() {
        var contents = String.join(
                ", ",
                Arrays.stream(this.backing).map(KnValue::getDebugRepresentation).toList()
        );

        return "[" + contents + "]";
    }
}
