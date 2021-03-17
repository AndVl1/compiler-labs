package ru.bmstu.iu9.token

import ru.bmstu.iu9.Position
import ru.bmstu.iu9.Tag

class NumberToken(
    value: String,
    start: Position,
    end: Position,
): Token(Tag.NUMBER, start, end) {

    val value : Int = Integer.parseInt(value, 16)

    val valueHex: String
    get() =  if (hasZero)
        "0${Integer.toHexString(value)}"
    else
        Integer.toHexString(value)

    private val hasZero = value[0] == '0'

    override fun toString(): String {
        return "NUMBER ${super.toString()}: $value"
    }
}
