package com.github.rikashore.noctilus.syntax.rewriting;

public class Token {
    public static abstract class WithLiteral<T> extends TokenBase {
        public T literal;
    }

    public static abstract class WithoutLiteral extends TokenBase {}
}
