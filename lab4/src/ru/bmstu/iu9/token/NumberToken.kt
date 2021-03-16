package ru.bmstu.iu9.token

import ru.bmstu.iu9.Position
import ru.bmstu.iu9.Tag

class NumberToken(
    val value: String,
    start: Position,
    end: Position,
): Token(Tag.NUMBER, start, end) {
    override fun toString(): String {
        return "NUMBER ${super.toString()}: $value"
    }
}
