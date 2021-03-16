package ru.bmstu.iu9.token

import ru.bmstu.iu9.Position
import ru.bmstu.iu9.Tag

class UnknownToken(
    tag: Tag,
    start: Position,
    end: Position,
) : Token(tag, start, end)