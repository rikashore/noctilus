package com.github.rikashore.noctilus.syntax.rewriting.tokens;

import com.github.rikashore.noctilus.syntax.rewriting.Token;

public class ErrorToken extends Token.WithLiteral<String> {
    public final String message;

    public ErrorToken(int line, String val, String message) {
        this.line = line;
        this.literal = val;
        this.message = message;
    }

    public String getErrMessage() {
        return this.message.formatted(this.line, this.literal);
    }

    @Override
    public String toString() {
        return "Error Token";
    }
}
