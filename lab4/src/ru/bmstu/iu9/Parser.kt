package ru.bmstu.iu9

class Parser {
    private val messages = ArrayList<Message>()
    fun addMessage(msg: Message) {
        messages.add(msg)
    }
}