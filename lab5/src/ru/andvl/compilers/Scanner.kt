package ru.andvl.compilers

import ru.andvl.compilers.token.*
import java.io.IOException
import java.io.InputStream
import java.io.PushbackInputStream

class Scanner(
    private val compiler: Compiler,
    private val inputStream: InputStream,
): Iterator<Token> {

    private val pushbackInputStream = PushbackInputStream(inputStream)

    private val states = arrayOf(
        /*         \n   \s  d   e   f   v   a   r   l   \d  \w  '   [   ]   */
        intArrayOf( 0,  0,  6,  4,  4,  9,  4,  4,  4,  11, 4,  5,  1,  2), // 0 StartState
        intArrayOf(-1,  -1, 6,  4,  4,  9,  4,  4,  4,  11, 4,  -1, -1, -1), // 1 OpeningParen
        intArrayOf(-1,  -1, 6,  4,  4,  9,  4,  4,  4,  11, 4,  -1, -1, -1), // 2 ClosingParen
        intArrayOf(-1,  -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5,  -1, -1), // 3 StringLiteral
        intArrayOf(-1,  -1, 4,  4,  4,  4,  4,  4,  4,  4,  4,  -1, -1, -1), // 4 Ident
        intArrayOf(-1,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  3,  5,  5), // 5 String
        intArrayOf(-1,  -1, 4,  7,  4,  4,  4,  4,  4,  4,  4,  -1, -1, -1), // 6 IdentD
        intArrayOf(-1,  -1, 4,  4,  8,  4,  4,  4,  4,  4,  4,  -1, -1, -1), // 7 IdentE
        intArrayOf(-1,  -1, 4,  4,  4,  4,  4,  4,  4,  4,  4,  -1, -1, -1), // 8 Keyword
        intArrayOf(-1,  -1, 4,  4,  4,  4,  10, 4,  4,  4,  4,  -1, -1, -1), // 9 IdentV
        intArrayOf(-1,  -1, 4,  4,  4,  4,  4,  8,  8,  4,  4,  -1, -1, -1), // 10 IdentA
        intArrayOf(-1,  -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1), // 11 Integer
    )

    private val finalStates = booleanArrayOf(
        false, true, true, true, true, false, true, true, true, true, true, true
    )

    private var currentState = 0
    private var currentLine = 1
    private var currentColumn = 0
    private var nextToken: Token? = null

    private fun readNext(): Int {
        var r = -1
        try {
            r = pushbackInputStream.read()
        } catch (e: IOException) {
            compiler.addMessage(
                Message(
                    Message.Type.FATAL_ERROR,
                    Position(currentLine, currentColumn),
                    e.message
                )
            )
            e.printStackTrace()
        }
        return r
    }

    /* \n   \s  d   e   f   v   a   r   l   \d  \w  '   [   ]   */
    private fun getState(c: Char): Int {
        when (c) {
            '\n' -> return 0
            ' ', '\t' -> return 1
            'd' -> return 2
            'e' -> return 3
            'f' -> return 4
            'v' -> return 5
            'a' -> return 6
            'r' -> return 7
            'l' -> return 8
            '\'' -> return 11
            '[' -> return 12
            ']' -> return 13
            else -> return if (c in '0'..'9') {
                9
            } else if (c in 'a'..'z' || c in 'A'..'Z') {
                10
            } else {
                14
            }
        }
    }

    override fun hasNext(): Boolean {
        val sb = StringBuilder()
        var r: Int
        while (readNext().also { r = it } > -1) {
            val c = r.toChar()
            currentColumn++
            val stateIndex: Int = getState(c)
            val newState = states[currentState][stateIndex]
            if (newState != -1) {
                if (currentState != 5){
                    when (stateIndex) {
                        0 -> {
                            currentLine++
                            currentColumn = 0
                        }
                        1 -> {
                            // do nothing
                        }
                        else -> sb.append(c)
                    }
                } else {
                    sb.append(c)
                }
                currentState = newState
                continue
            }
            if (!finalStates[currentState]) {
                sb.append(c)
                compiler.addMessage(
                    Message(
                        Message.Type.ERROR, Position(
                            currentLine,
                            currentColumn - sb.length + 1
                        ),
                        "Unexpected: ${sb.replace(Regex("\n"), """\\n""")}"
                    )
                )
                currentLine++
                currentColumn = 0
                currentState = 0
                sb.setLength(0)
                continue
            }
            break
        }

        if (r == -1) {
            currentColumn++
        }

        val lexeme = sb.toString()
        val startPos = Position(
            currentLine,
            currentColumn - lexeme.length
        )
        val endPos = Position(
            currentLine,
            currentColumn - 1
        )
        when(currentState) {
            1 -> {
                nextToken = TokenParen(startPos, endPos, lexeme)
            }
            2 -> {
                nextToken = TokenParen(startPos, endPos, lexeme)
            }
            3 -> {
                nextToken = TokenStringLiteral(startPos, endPos, lexeme)
            }
            4, 6, 7, 9, 10 -> {
                nextToken = TokenID(startPos, endPos, lexeme)
            }
            5 -> {
                // not final state
            }
            8 -> {
                nextToken = TokenKeyword(startPos, endPos, lexeme)
            }
            11 -> {
                nextToken = TokenInteger(startPos, endPos, lexeme.toInt())
            }
        }
        if(r > -1) {
            try {
                pushbackInputStream.unread(r)
            } catch (e: IOException) {
                compiler.addMessage(
                    Message(
                        Message.Type.FATAL_ERROR,
                        Position(
                            currentLine,
                            currentColumn
                        ),
                        e.message
                    )
                )
            }
            currentColumn--
        }
        currentState = 0

        return nextToken != null
    }

    override fun next(): Token {
        val token = nextToken ?: throw NoSuchElementException()
        nextToken = null
        return token
    }
}
