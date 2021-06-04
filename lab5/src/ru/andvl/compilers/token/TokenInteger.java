package ru.andvl.compilers.token;

import ru.andvl.compilers.Position;

public class TokenInteger extends Token {
    
    private final int literal;
    
    public TokenInteger(Position startPosition, Position endPosition, int literal) {
        super(startPosition, endPosition);
        this.literal = literal;
    }
    
    @Override
    public String toString() {
        return "INTEGER LITERAL " + this.startPosition + "-" + this.endPosition + ": " + this.literal;
    }
    
    public int getLiteral() {
        return this.literal;
    }
    
}
