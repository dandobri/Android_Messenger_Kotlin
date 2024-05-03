package ru.ok.itmo.example

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ok.itmo.example.chats.MessageDivideData

class MessageList: ViewModel() {
    private val messages = MutableLiveData<MutableList<MessageDivideData>>(mutableListOf())
    val messagesData: LiveData<out List<MessageDivideData>> = messages
    fun addMessage(message: MessageDivideData) {
        messages.value?.add(message)
    }
}