package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ok.itmo.example.chats.ChatsDescription
import ru.ok.itmo.example.chats.Message
import ru.ok.itmo.example.chats.MessageDivideData
import ru.ok.itmo.example.logins.ChatApi

class MessageFragment(private val chatApi: ChatApi, private val chatsDescription: ChatsDescription): Fragment(R.layout.chat_fragment) {
    private lateinit var listMessages: MessageList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listMessages = ViewModelProvider(this)[MessageList::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chatData = view.findViewById<TextView>(R.id.chat_data)
        val chatName = chatsDescription.name
        chatData.text = chatName
        val progressBar = view.findViewById<ProgressBar>(R.id.messagesLoading)
        progressBar.isVisible = true
        val messages = chatApi.getChat(chatName)
        messages.enqueue(object : Callback<List<Message>> {
            override fun onResponse(p0: Call<List<Message>>, p1: Response<List<Message>>) {
                p1.body()!!.forEach { message ->
                    message.data.Image?.link?.let {
                        chatApi.getImage(it).enqueue(object : Callback<String> {
                            override fun onResponse(p0: Call<String>, p1: Response<String>) {
                                listMessages.addMessage(
                                    MessageDivideData(message.id, message.from, message.to,
                                        null, p1.body()!!, message.time)
                                )
                            }
                            override fun onFailure(p0: Call<String>, p1: Throwable) {}
                        })
                    } ?: listMessages.addMessage(
                        MessageDivideData(message.id, message.from, message.to, message.data.Text?.text!!, null, message.time)
                    )
                }
                val messagesRecyclerView = view.findViewById<RecyclerView>(R.id.messages)
                progressBar.isVisible = false
                listMessages.messagesData.observe(viewLifecycleOwner) {
                    progressBar.isVisible = false
                    if(listMessages.messagesData.value.isNullOrEmpty()) {
                        view.findViewById<TextView>(R.id.null_messages).isVisible = true
                    } else {
                        messagesRecyclerView.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = MessageView(it)
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<List<Message>>, p1: Throwable) {}
        })
    }
}