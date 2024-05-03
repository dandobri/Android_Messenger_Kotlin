package ru.ok.itmo.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.chats.ChatsDescription

class ChatView(
    private val chats: List<ChatsDescription>,
    private val onClick: (ChatsDescription) -> Unit
) : RecyclerView.Adapter<ChatView.Chat>() {
    class Chat(view: View) : RecyclerView.ViewHolder(view) {
        //private val image = view.findViewById<AvatarView>(R.id.photo_card)
        private val chatName = view.findViewById<TextView>(R.id.chat_name)
        private val chatDescription = view.findViewById<TextView>(R.id.chat_description)
        private val chatTime = view.findViewById<TextView>(R.id.chat_time)
        val chatView = view.findViewById<ConstraintLayout>(R.id.chatCard)

        fun bind(chat: ChatsDescription) {
            //image.setText(chat.name)
            chatName.text = chat.name
            chatDescription.text = chat.description
            chatTime.text = chat.time.toString()
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Chat {
        val chat =  Chat(LayoutInflater.from(p0.context).inflate(R.layout.chat_view, p0, false))
        chat.chatView.setOnClickListener {
            onClick(chats[chat.adapterPosition])
        }
        return chat
    }

    override fun getItemCount(): Int {
        return 15
    }

    override fun onBindViewHolder(p0: Chat, p1: Int) {
        if (chats.isNotEmpty()) {
            return p0.bind(chats[p1])
        }
    }
}