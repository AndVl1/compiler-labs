package ru.andvl.compilers;

public class Position {
    
    private final int lineNumber, columnNumber;
    
    public Position(int lineNumber, int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
    
    @Override
    public String toString() {
        return "(" + this.lineNumber + ", " + this.columnNumber + ")";
    }
    
    int getLineNumber() {
        return this.lineNumber;
    }
    
    int getColumnNumber() {
        return this.columnNumber;
    }
    
}
