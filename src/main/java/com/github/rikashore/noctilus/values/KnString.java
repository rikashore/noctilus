package com.github.rikashore.noctilus.values;

import java.util.stream.Collectors;

public class KnString extends KnValue {
    public final String value;

    public KnString(String value) {
        this.value = value;
    }

    @Override
    public String getDebugRepresentation() {
        return this.value.codePoints()
                .mapToObj(b -> switch (b) {
                    case 0x09 -> "\\t";
                    case 0x0A -> "\\n";
                    case 0x0D -> "\\r";
                    case 0x5C -> "\\";
                    case 0x22 -> "\"";
                    default -> Character.toString(b);
                })
                .collect(Collectors.joining());
    }
}
