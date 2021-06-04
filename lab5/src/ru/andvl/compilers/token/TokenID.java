package ru.andvl.compilers.token;

import ru.andvl.compilers.Position;

public class TokenID extends Token {
    
    private final String lexeme; //TODO: хранить лексемы идентификаторов можно в таблице... (оптимизация)
    
    public TokenID(Position startPosition, Position endPosition, String lexeme) {
        super(startPosition, endPosition);
        this.lexeme = lexeme;
    }
    
    @Override
    public String toString() {
        return "ID " + this.startPosition + "-" + this.endPosition + ": " + this.lexeme;
    }
    
    public String getLexeme() {
        return this.lexeme;
    }
    
}
