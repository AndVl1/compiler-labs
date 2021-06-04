package ru.andvl.compilers

import java.io.*


class Compiler (filename: String) {
    private val fileInputStream: InputStream = FileInputStream(filename)
    private val messages = ArrayList<Message>()

    init {
        val file = "${fileInputStream.getFileContent()}\n".byteInputStream()

        val scanner = Scanner(this, file)

        while (scanner.hasNext()) {
            println(scanner.next())
        }

        fileInputStream.close()
    }

    fun addMessage(message: Message) {
        messages.add(message)
    }

    fun getMessages(): ArrayList<Message> = messages
}

fun InputStream.getFileContent(): String {
    BufferedReader(InputStreamReader(this)).use { br ->
        val sb = StringBuilder()
        var line: String?
        while (br.readLine().also { line = it } != null) {
            sb.append(line)
            sb.append('\n')
        }
        return sb.toString()
    }
}
