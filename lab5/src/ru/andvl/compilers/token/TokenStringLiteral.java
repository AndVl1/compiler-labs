package ru.andvl.compilers.token;

import kotlin.text.Regex;
import ru.andvl.compilers.Position;

public class TokenStringLiteral extends Token {
    
    private final String lexeme;
    
    public TokenStringLiteral(Position startPosition, Position endPosition, String lexeme) {
        super(startPosition, endPosition);
        this.lexeme = lexeme.substring(1, lexeme.length() - 1).replace("''", "'");
    }
    
    @Override
    public String toString() {
        return "STRING LITERAL " + this.startPosition + "-" + this.endPosition + ": " + this.lexeme;
    }
    
    public String getLexeme() {
        return this.lexeme;
    }
    
}
