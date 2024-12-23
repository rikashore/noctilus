package com.github.rikashore.noctilus.syntax.lexing;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private static final List<Character> symbols;

    static {
        symbols = new ArrayList<>();
        symbols.add('@');
        symbols.add(':');
        symbols.add('!');
        symbols.add('~');
        symbols.add(',');
        symbols.add('[');
        symbols.add(']');
        symbols.add('+');
        symbols.add('-');
        symbols.add('*');
        symbols.add('/');
        symbols.add('%');
        symbols.add('^');
        symbols.add('<');
        symbols.add('>');
        symbols.add('?');
        symbols.add('&');
        symbols.add('|');
        symbols.add(';');
        symbols.add('=');
    }

    private int pos = 0;
    private int line = 1;

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> scan() {
        while (!isAtEnd()) {
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, String.valueOf(Character.MIN_VALUE), line));
        return tokens;
    }

    private void scanToken() {
        var curr = advance();

        var token = switch (curr) {
            case ' ', '\r', '\t', '(', ')', ':' -> new Token(TokenType.Fluff, String.valueOf(curr), line);

            case '\n' -> {
                line++;
                yield new Token(TokenType.Fluff, String.valueOf(curr), line);
            }

            case '#' -> {
                while (peek() != '\n' && !isAtEnd()) advance();
                yield new Token(TokenType.Fluff, String.valueOf('#'), line);
            }

            case '"', '\'' -> {
                var startMark = pos;
                while (peek() != curr && !isAtEnd()) advance();

                if (isAtEnd()) {
                    throw new RuntimeException("Unterminated String");
                }


                advance();

                var val = source.substring(startMark, pos - 1);
                yield new Token(TokenType.StringLiteral, val, line);
            }

            default -> {
                if (Character.isDigit(curr)) {
                    var marker = pos - 1;

                    while (Character.isDigit(peek())) advance();

                    var val = source.substring(marker, pos);
                    yield new Token(TokenType.IntLiteral, val, line);
                }

                if (Character.isUpperCase(curr)) {
                    var marker = pos - 1;

                    while (Character.isUpperCase(peek()) || peek() == '_') advance();

                    var val = source.substring(marker, marker + 1);
                    yield new Token(TokenType.Upper, val, line);
                }

                if (Character.isLowerCase(curr)) {
                    var marker = pos - 1;

                    while (Character.isLowerCase(peek()) || peek() == '_') advance();

                    var val = source.substring(marker, pos);
                    yield new Token(TokenType.Name, val, line);
                }

                if (symbols.contains(curr)) {
                    yield new Token(TokenType.Sym, Character.toString(curr), line);
                }

                yield new Token(TokenType.Unknown, String.valueOf(curr), line);
            }
        };

        tokens.add(token);
    }

    private char advance() {
        return source.charAt(pos++);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(pos);
    }

    private boolean isAtEnd() {
        return pos >= source.length();
    }
}
