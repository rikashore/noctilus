package com.github.rikashore.noctilus.syntax.rewriting;

import com.github.rikashore.noctilus.syntax.lexing.Token;
import com.github.rikashore.noctilus.syntax.lexing.TokenType;
import com.github.rikashore.noctilus.syntax.rewriting.tokens.*;

import java.util.List;

import com.github.rikashore.noctilus.syntax.KnownFunctions;

public class Rewriter {
    private final List<Token> tokens;

    public Rewriter(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<TokenBase> rewrite() {
        return tokens.stream()
                .filter(t -> t.type() != TokenType.Fluff)
                .map(t -> switch (t.type()) {
                    case TokenType.Sym -> new FunctionToken(t.line(), t.lexeme(), KnownFunctions.get(t.lexeme()));
                    case TokenType.Name -> new IdentifierToken(t.line(), t.lexeme());
                    case TokenType.Upper -> {
                        var arity = KnownFunctions.get(t.lexeme());

                        if (arity == null) {
                            yield new ErrorToken(t.line(), t.lexeme(), "[%d] Unknown function %s");
                        } else {
                            yield new FunctionToken(t.line(), t.lexeme(), arity);
                        }
                    }
                    case StringLiteral -> new StringToken(t.line(), t.lexeme());
                    case IntLiteral -> {
                        try {
                            var intVal = Integer.parseInt(t.lexeme());
                            yield new IntToken(t.line(), intVal);
                        } catch (NumberFormatException e) {
                            yield new ErrorToken(t.line(), t.lexeme(), "[%d] Integer (%s) too large");
                        }
                    }
                    case EOF -> new EOFToken(t.line());
                    case Fluff -> throw new IllegalStateException("All Fluff tokens will have been filtered.");
                    case Unknown -> new ErrorToken(t.line(), t.lexeme(), "[%d] Unknown token %s");
                })
                .toList();
    }


}
