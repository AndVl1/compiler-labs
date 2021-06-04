package ru.andvl.compilers.token

import ru.andvl.compilers.Position

class TokenParen(
    startPosition: Position?,
    endPosition: Position?,
    private val lexeme: String
) : Token(startPosition, endPosition) {

    override fun toString(): String {
        return "PAREN $startPosition-$endPosition: $lexeme"
    }

}
