package ru.bmstu.iu9.token

import ru.bmstu.iu9.Position
import ru.bmstu.iu9.Tag

class KeywordToken(
    val value: String,
    start: Position,
    end: Position,
): Token(Tag.KEYWORD, start, end) {
    override fun toString(): String {
        return "KEYWORD ${super.toString()}: $value"
    }
}
