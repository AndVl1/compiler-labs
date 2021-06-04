package ru.andvl.compilers

import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Usage: compiler <program filename>")
        exitProcess(1)
    }

    val compiler = Compiler(args[0])
    var error = 0
    var fatalError = 0
    println()
    for (message in compiler.getMessages()) {
        println(message)
        when (message.type) {
            Message.Type.ERROR -> error++
            Message.Type.FATAL_ERROR -> fatalError++
            else -> {}
        }
    }
    println()
    println("Errors: $error")
    println("Fatal errors: $fatalError")

}
