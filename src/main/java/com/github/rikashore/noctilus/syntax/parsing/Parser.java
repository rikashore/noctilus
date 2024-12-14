package com.github.rikashore.noctilus.syntax.parsing;

import com.github.rikashore.noctilus.syntax.rewriting.TokenBase;
import com.github.rikashore.noctilus.syntax.rewriting.tokens.FunctionToken;
import com.github.rikashore.noctilus.syntax.rewriting.tokens.IdentifierToken;
import com.github.rikashore.noctilus.syntax.rewriting.tokens.IntToken;
import com.github.rikashore.noctilus.syntax.rewriting.tokens.StringToken;
import com.github.rikashore.noctilus.values.*;

import java.util.List;

public class Parser {
    private final TokenBase[] tokens;

    private int current = 0;

    public Parser(List<TokenBase> tokens) {
        this.tokens = tokens.toArray(tokens.toArray(new TokenBase[0]));
    }

    public KnFunction parse() {
        var f = parseNextValue();

        if (!(f instanceof KnFunction)) {
            throw new IllegalStateException("Knight program does not start with function");
        }

        return (KnFunction) f;
    }

    private KnValue parseNextValue() {
        var value = switch (tokens[current]) {
            case IdentifierToken i -> new KnVariable(i.literal);
            case IntToken in -> new KnInt(in.literal);
            case StringToken s -> new KnString(s.literal);
            case FunctionToken f -> {
                if (f.name.equals("T")) {
                    yield new KnBool(true);
                } else if (f.name.equals("F")) {
                    yield new KnBool(false);
                }

                var args = new KnValue[f.arity];

                for (int i = 0; i < f.arity; i++) {
                    current++;
                    args[i] = parseNextValue();
                }

                yield new KnFunction(f.name, args);
            }
            default -> throw new RuntimeException("Unexpected value");
        };
        return value;
    }
}
