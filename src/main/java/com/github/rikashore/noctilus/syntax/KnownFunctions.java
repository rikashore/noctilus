package com.github.rikashore.noctilus.syntax;

import java.util.HashMap;

public class KnownFunctions {
    private static final HashMap<String, Integer> knownFunctions;

    static {
        knownFunctions = new HashMap<>();
        knownFunctions.put("@", 0);
        knownFunctions.put("T", 0);
        knownFunctions.put("F", 0);
        knownFunctions.put("N", 0);
        knownFunctions.put("P", 0);
        knownFunctions.put("R", 0);

        knownFunctions.put("!", 1);
        knownFunctions.put("~", 1);
        knownFunctions.put(",", 1);
        knownFunctions.put("[", 1);
        knownFunctions.put("B", 1);
        knownFunctions.put("C", 1);
        knownFunctions.put("Q", 1);
        knownFunctions.put("D", 1);
        knownFunctions.put("O", 1);
        knownFunctions.put("L", 1);
        knownFunctions.put("A", 1);

        knownFunctions.put("+", 2);
        knownFunctions.put("-", 2);
        knownFunctions.put("*", 2);
        knownFunctions.put("/", 2);
        knownFunctions.put("%", 2);
        knownFunctions.put("^", 2);
        knownFunctions.put("<", 2);
        knownFunctions.put(">", 2);
        knownFunctions.put("&", 2);
        knownFunctions.put("?", 2);
        knownFunctions.put("|", 2);
        knownFunctions.put(";", 2);
        knownFunctions.put("=", 2);
        knownFunctions.put("W", 2);

        knownFunctions.put("I", 3);
        knownFunctions.put("G", 3);

        knownFunctions.put("S", 4);
    }

    public static Integer get(String key) {
        return knownFunctions.get(key);
    }
}
