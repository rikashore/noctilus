package com.github.rikashore.noctilus.syntax.rewriting.tokens;

import com.github.rikashore.noctilus.syntax.rewriting.Token;

public class IntToken extends Token.WithLiteral<Integer> {
    public IntToken(int line, int value) {
        this.line = line;
        this.literal = value;
    }

    @Override
    public String toString() {
        return String.format("Integer [%d]: %d", this.line, this.literal);
    }
}
