package com.github.rikashore.noctilus.syntax.rewriting.tokens;

import com.github.rikashore.noctilus.syntax.rewriting.Token;

public class IdentifierToken extends Token.WithLiteral<String> {
    public IdentifierToken(int line, String ident) {
        this.line = line;
        this.literal = ident;
    }

    @Override
    public String toString() {
        return String.format("Identifier [%d]: (%s)", this.line, this.literal);
    }
}
