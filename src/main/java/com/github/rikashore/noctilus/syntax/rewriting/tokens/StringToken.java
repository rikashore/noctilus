package com.github.rikashore.noctilus.syntax.rewriting.tokens;

import com.github.rikashore.noctilus.syntax.rewriting.Token;

public class StringToken extends Token.WithLiteral<String> {
    public StringToken(int line, String value) {
        this.line = line;
        this.literal = value;
    }

    @Override
    public String toString() {
        return String.format("String [%d]: %s", this.line, this.literal);
    }
}
