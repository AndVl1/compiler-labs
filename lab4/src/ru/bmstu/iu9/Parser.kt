package ru.bmstu.iu9

class Parser {
    private val _messages = ArrayList<Message>()
    val messages : MutableList<Message>
    get() = _messages

    fun addMessage(msg: Message) {
        _messages.add(msg)
    }

    fun getErrorMessages() {
        for (msg in messages) {
            println(msg)
        }
    }
}