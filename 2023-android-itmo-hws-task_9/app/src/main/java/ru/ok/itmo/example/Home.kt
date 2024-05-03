package ru.ok.itmo.example

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.Callback
import ru.ok.itmo.example.chats.ChatsDescription
import ru.ok.itmo.example.chats.Data
import ru.ok.itmo.example.chats.Message
import ru.ok.itmo.example.chats.Text
import ru.ok.itmo.example.logins.ChatApi
import java.util.Date
import kotlin.concurrent.thread

class Home(private val chati: Retrofit): Fragment(R.layout.home_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chats = mutableListOf<ChatsDescription>()
        val progressBar = view.findViewById<ProgressBar>(R.id.allChannelsLoading)
        progressBar.isVisible = true
        val chatApi = ChatApi.create(chati)
        val chat = chatApi.getChats()
        chat.enqueue(object : Callback<List<String>> {
            override fun onResponse(p0: Call<List<String>>, p1: Response<List<String>>) {
                val chatNames = p1.body()!!
                var countChats = chatNames.size
                chatNames.forEach { chatName ->
                    chatApi.getChat(chatName).enqueue(object : Callback<List<Message>> {
                        override fun onResponse(p0: Call<List<Message>>, p1: Response<List<Message>>) {
                            val message = p1.body()!!.last()
                            chats.add(ChatsDescription(chatName,
                                message.data.Text?.text ?: "no messages", Date(message.time)))
                            countChats--
                        }
                        override fun onFailure(p0: Call<List<Message>>, p1: Throwable) {}
                    })
                }
                thread {
                    if(countChats > 0) {
                        Thread.sleep(500)
                    }
                    requireActivity().runOnUiThread {
                        progressBar.isVisible = false
                        val newChat = view.findViewById<RecyclerView>(R.id.allChats)
                        newChat.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = ChatView(chats) {
                                val messageFragment = MessageFragment(chatApi, it)
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.container, messageFragment).addToBackStack(null).commit()
                            }
                        }
                    }
                }
            }
            override fun onFailure(p0: Call<List<String>>, p1: Throwable) {
            }
        })
        view.findViewById<Button>(R.id.new_chat).setOnClickListener {
            chatApi.createChat("newChat",
                Message(239, "Danya", "Drug", Data(Text("Privet"), null), System.currentTimeMillis())
            )
        }
    }
}