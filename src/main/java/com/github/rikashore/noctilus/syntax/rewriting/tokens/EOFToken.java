package com.github.rikashore.noctilus.syntax.rewriting.tokens;

import com.github.rikashore.noctilus.syntax.rewriting.Token;

public class EOFToken extends Token.WithoutLiteral {
    public EOFToken(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "EOF";
    }
}
