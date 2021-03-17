package ru.bmstu.iu9

import ru.bmstu.iu9.token.IdentToken
import ru.bmstu.iu9.token.KeywordToken
import ru.bmstu.iu9.token.NumberToken
import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        error("no input file")
    }

    val file = File(args[0]).readBytes().toString(Charsets.UTF_8)
    println(file)
    val parser = Parser()
    val sc = Scanner(file, parser)
    while (true) {
        val tok = sc.nextToken()
        if (tok.tag == Tag.END_OF_PROGRAM) {
            break
        } else {
            print("${tok.tag} ${tok.coordinates}: ${when (tok) {
                is IdentToken -> tok.value
                is KeywordToken -> tok.value
                else -> (tok as NumberToken).value
            }}\n")
        }
    }
    if (parser.messages.isNotEmpty()) {
        println("errors:")
        parser.getErrorMessages()
    }
}
