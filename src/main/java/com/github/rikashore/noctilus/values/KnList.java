package com.github.rikashore.noctilus.values;

import java.util.ArrayList;
import java.util.List;

public class KnList extends KnValue {
    public final List<KnValue> backing;

    private KnList(List<KnValue> backing) {
        this.backing = backing;
    }

    public static KnList empty() {
        return new KnList(new ArrayList<>());
    }

    public static KnList fromElements(List<KnValue> values) {
        return new KnList(values);
    }

    public KnList append(KnValue value) {
        var newBacking = new ArrayList<>(this.backing);
        newBacking.add(value);
        return KnList.fromElements(newBacking);
    }

    @Override
    public String getDebugRepresentation() {
        var contents = String.join(
                ", ",
                this.backing.stream().map(KnValue::getDebugRepresentation).toList()
        );

        return "[" + contents + "]";
    }
}
