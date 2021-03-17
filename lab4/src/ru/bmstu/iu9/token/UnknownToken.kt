package ru.bmstu.iu9.token

import ru.bmstu.iu9.Position
import ru.bmstu.iu9.Tag

class UnknownToken(
    tag: Tag,
    start: Position,
    end: Position,
    val value: String,
) : Token(tag, start, end) {
    override fun toString(): String {
        return "UNRECOGNIZED ${super.coordinates}: $value"
    }
}