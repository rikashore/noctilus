package com.github.rikashore.noctilus.syntax.rewriting.tokens;

import com.github.rikashore.noctilus.syntax.rewriting.Token;

public class FunctionToken extends Token.WithoutLiteral {
    public final String name;
    public final int arity;

    public FunctionToken(int line, String name, int arity) {
        this.line = line;
        this.name = name;
        this.arity = arity;
    }

    @Override
    public String toString() {
        return String.format("Function Call [%d]: (%s) expressions: (%d)", this.line, this.name, this.arity);
    }
}
