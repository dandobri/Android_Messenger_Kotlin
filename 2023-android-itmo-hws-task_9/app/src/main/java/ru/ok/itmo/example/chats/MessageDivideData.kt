package ru.ok.itmo.example.chats

data class MessageDivideData(
    val id: Long,
    val from: String,
    val to: String,
    val text: String?,
    val img: String?,
    val time: Long
)