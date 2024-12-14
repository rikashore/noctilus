package com.github.rikashore.noctilus.syntax.lexing;

public record Token(TokenType type, String lexeme, int line) {
}
