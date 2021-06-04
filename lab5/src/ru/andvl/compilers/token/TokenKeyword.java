package ru.andvl.compilers.token;

import ru.andvl.compilers.Position;

public class TokenKeyword extends Token {
    
    private final String lexeme;
    
    public TokenKeyword(Position startPosition, Position endPosition, String lexeme) {
        super(startPosition, endPosition);
        this.lexeme = lexeme;
    }
    
    @Override
    public String toString() {
        return "KEYWORD " + this.startPosition + "-" + this.endPosition + ": " + this.lexeme;
    }
    
    public String getLexeme() {
        return this.lexeme;
    }
    
}
