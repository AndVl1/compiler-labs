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
//                parser.addMessage(Message("Unrecognized token", currentToken))
                currentToken = token.coordinates.end
            } else {
                currentToken = token.coordinates.end
                return token
            }
        }
        return UnknownToken(Tag.END_OF_PROGRAM, currentToken, currentToken, value = "")
    }

    private fun readNumber(position: Position): Token {
        var isError = false
        var p = position.next()
        val number = StringBuilder()
        number.append(position.code.toChar())
        while (!p.isEOF && !p.isWhitespace) {
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
                number.append(p.code.toChar())
                isError = true
            }
            p = p.next()
        }
        return if (!isError) {
            NumberToken(
                value = number.toString(),
                start = position,
                end = p,
            )
        } else {
            UnknownToken(
                Tag.UNKNOWN,
                start = position,
                end = p,
                value = number.toString()
            )
        }
    }

    private fun readWord(position: Position): Token {
        var p = position.next()
        val first = position
        var last = p
        val word = StringBuilder()
        word.append(position.code.toChar())
        while (!p.isEOF) {
            if (p.isNewLine || p.isWhitespace) {
                return if (word.first() != word.last()) {
                    parser.addMessage(Message("Should start with same letter", p))
                    UnknownToken(
                        tag = Tag.UNKNOWN,
                        start = position,
                        end = p,
                        value = word.toString()
                    )
                } else when (word.toString()) {
                    "qeq", "xx", "xxx" -> KeywordToken(
                        value = word.toString(),
                        start = position,
                        end = p,
                    )
                    else -> IdentToken(
                        value = word.toString(),
                        start = position,
                        end = p,
                    )
                }
            }
            last = p
            word.append(p.code.toChar())
            p = p.next()
        }
        return if(word.first() != word.last()){
            parser.addMessage(Message("Should start with same letter", p))
            UnknownToken(
                tag = Tag.UNKNOWN,
                start = position,
                end = p,
                value = word.toString()
            )
        } else
            when (word.toString()) {
            "qeq", "xx", "xxx" -> KeywordToken(
                value = word.toString(),
                start = position,
                end = p
            )
            else -> IdentToken(word.toString(), position, p)
        }
    }
}
