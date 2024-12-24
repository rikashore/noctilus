package com.github.rikashore.noctilus;

import com.github.rikashore.noctilus.interpretation.Interpreter;
import com.github.rikashore.noctilus.syntax.lexing.Lexer;
import com.github.rikashore.noctilus.syntax.parsing.Parser;
import com.github.rikashore.noctilus.syntax.rewriting.Rewriter;
import com.github.rikashore.noctilus.syntax.rewriting.tokens.ErrorToken;

public class Main {
    public static void main(String[] args) {
        var source = """
                ; = maximum 100
                ; = i 0
                
                : WHILE < i maximum
                    ; = i + i 1
                    : OUTPUT
                        : IF ! % i 15 'FizzBuzz'
                        : IF ! % i 5 'Fizz'
                        : IF ! % i 3 'Buzz' i""";

        var lexer = new Lexer(source);
        var tokens = lexer.scan();

        var rewriter = new Rewriter(tokens);
        var rewrittenTokens = rewriter.rewrite();

        var errors = rewrittenTokens
                .stream()
                .filter(t -> t instanceof ErrorToken)
                .map(t -> (ErrorToken)t)
                .toList();

        if (!errors.isEmpty()) {
            errors.stream()
                    .map(ErrorToken::getErrMessage)
                    .forEach(System.out::println);

            System.exit(1);
        }

        var parser = new Parser(rewrittenTokens);
        var fn = parser.parse();

        var interpreter = new Interpreter(fn);
        interpreter.run();
    }
}