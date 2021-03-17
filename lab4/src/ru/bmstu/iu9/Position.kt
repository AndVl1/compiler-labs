package ru.bmstu.iu9


data class Coordinates(
    val start: Position,
    val end: Position,
) {
    override fun toString(): String {
        return "$start - $end"
    }
}

class Position {
    var text: String
        private set
    var line: Int
        private set
    var pos: Int
        private set
    var index: Int
        private set

    constructor(text: String) {
        this.text = text
        pos = 1
        line = pos
        index = 0
    }

    constructor(p: Position) {
        text = p.text
        line = p.line
        pos = p.pos
        index = p.index
    }

    override fun toString(): String {
        return "($line, $pos)"
    }

    val isEOF: Boolean
        get() = index == text.length
    val code: Int
        get() = if (isEOF) -1 else text.codePointAt(index)
    val isWhitespace: Boolean
        get() = !isEOF && Character.isWhitespace(code)
    val isDecimalDigit: Boolean
        get() = !isEOF && text[index] >= '0' && text[index] <= '9'
    val isLetter: Boolean
        get() = !isEOF && Character.isLetter(code)
    val isNewLine: Boolean
        get() {
            if (index == text.length) {
                return true
            }
            return if (text[index] == '\r' && index + 1 < text.length) {
                text[index + 1] == '\n'
            } else text[index] == '\n'
        }

    operator fun next(): Position {
        val p = Position(this)
        if (!p.isEOF) {
            if (p.isNewLine) {
                if (p.text[p.index] == '\r') p.index++
                p.line++
                p.pos = 1
            } else {
                if (Character.isHighSurrogate(p.text[p.index])) p.index++
                p.pos++
            }
            p.index++
        }
        return p
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (text != other.text) return false
        if (line != other.line) return false
        if (pos != other.pos) return false
        if (index != other.index) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + line
        result = 31 * result + pos
        result = 31 * result + index
        return result
    }


}
