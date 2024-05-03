package ru.ok.itmo.example

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.chats.MessageDivideData

class MessageView(
    private val messages: List<MessageDivideData>
) : RecyclerView.Adapter<MessageView.Message>() {
    class Message(view: View): RecyclerView.ViewHolder(view) {
        private val messageFrom = view.findViewById<TextView>(R.id.messageFrom)
        private val messageText = view.findViewById<TextView>(R.id.messageText)
        private val messageFromAvatar = view.findViewById<ImageView>(R.id.messageFromAvatar)
        private val messageTime = view.findViewById<TextView>(R.id.messageTime)
        fun bind(message: MessageDivideData) {
            messageFrom.text = message.from
            messageText.text = message.text ?: "no message"
            messageTime.text = message.time.toString()
            message.img?.let {
                val decode: ByteArray = Base64.decode(it, Base64.DEFAULT)
                messageFromAvatar.setImageBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.size))
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Message {
        return Message(LayoutInflater.from(p0.context).inflate(R.layout.message_view, p0, false))
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(p0: Message, p1: Int) {
        if (messages.isNotEmpty()) {
            p0.bind(messages[p1])
        }
    }
}