package ru.andvl.compilers.token;

import ru.andvl.compilers.Position;

public abstract class Token {
    
    protected final Position startPosition, endPosition;
    
    public Token(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }
    
    public Position getStartPosition() {
        return this.startPosition;
    }

    public Position getEndPosition() {
        return this.endPosition;
    }
    
}
