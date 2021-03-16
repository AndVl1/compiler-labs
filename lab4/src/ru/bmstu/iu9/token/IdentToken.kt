package ru.bmstu.iu9.token

import ru.bmstu.iu9.Position
import ru.bmstu.iu9.Tag

class IdentToken(
    val value: String,
    start: Position,
    end: Position,
): Token(Tag.IDENT, start, end) {
    override fun toString(): String {
        return "IDENT ${super.toString()}: $value"
    }
}
