package ru.andvl.compilers;

import ru.andvl.compilers.Position;

public class Message {
    
    public enum Type {
        ERROR,
        FATAL_ERROR
    }
    
    private final Type type;
    private final Position position;
    private final String message;
    
    public Message(Type type, Position position, String message) {
        this.type = type;
        this.position = position;
        this.message = message;
    }
    
    @Override
    public String toString() {
        return this.type + " at " + this.position + ": " + this.message;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    public String getMessage() {
        return this.message;
    }
    
}
