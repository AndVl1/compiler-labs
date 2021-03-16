package ru.bmstu.iu9.token

import ru.bmstu.iu9.Coordinates
import ru.bmstu.iu9.Position
import ru.bmstu.iu9.Tag

abstract class Token (
    val tag: Tag,
    start: Position,
    end: Position,
) {
    val coordinates = Coordinates(start, end)

    override fun toString(): String {
        return coordinates.toString()
    }
}