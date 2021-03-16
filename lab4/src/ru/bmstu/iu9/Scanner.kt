package ru.bmstu.iu9

import ru.bmstu.iu9.token.*

class Scanner(
    program: String,
    private val parser: Parser,
) {
    private var currentToken = Position(program)

    fun nextToken(): Token {
        while (!currentToken.isEOF) {
            while (currentToken.isWhitespace) {
                currentToken = currentToken.next()
            }
            val token = when(currentToken.code) {
                '0'.toInt() -> readNumber(currentToken)
                else -> readWord(currentToken)
            }
            if (token.tag == Tag.UNKNOWN) {
                parser.addMessage(Message("Unrecognized token", currentToken))
                currentToken = currentToken.next()
            } else {
                currentToken = token.coordinates.end
                return token
            }
        }
        return UnknownToken(Tag.END_OF_PROGRAM, currentToken, currentToken)
    }

    private fun readNumber(position: Position): Token {
        var p = position.next()
        val number = StringBuilder()
        number.append(position.code.toChar())
        while (!p.isEOF) {
            if (p.code.toChar().isDigit() ||
                (p.code.toChar().toUpperCase() =='A') ||
                (p.code.toChar().toUpperCase() =='B') ||
                (p.code.toChar().toUpperCase() =='C') ||
                (p.code.toChar().toUpperCase() =='D') ||
                (p.code.toChar().toUpperCase() =='E') ||
                (p.code.toChar().toUpperCase() =='F')) {
                number.append(p.code.toChar())
            } else {
                parser.addMessage(Message("Not a hex number", p))
                return UnknownToken(Tag.UNKNOWN, position, p)
            }
            p = p.next()
        }
        return NumberToken(number.toString(), p, position)
    }

    private fun readWord(position: Position): Token {
        var p = position.next()
        val first = p
        var last = p
        val word = StringBuilder()
        word.append(position.code.toChar())
        while (!p.isEOF) {
            if (p.code == '\n'.toInt() || p.code == '\r'.toInt() || p.code == ' '.toInt()) {
                return if (first != last) {
                    parser.addMessage(Message("Should start with same letter", p))
                    UnknownToken(Tag.UNKNOWN, position, p)
                } else when (word.toString()) {
                    "qeq", "xx", "xxx" -> KeywordToken(word.toString(), position, p)
                    else -> IdentToken(word.toString(), position, p)
                }
            }
            last = p
            word.append(p.code.toChar())
            p = p.next()
        }
        return if (first != last) {
            UnknownToken(Tag.UNKNOWN, position, p)
        } else when (word.toString()) {
            "qeq", "xx", "xxx" -> KeywordToken(word.toString(), position, p)
            else -> IdentToken(word.toString(), position, p)
        }
    }
}