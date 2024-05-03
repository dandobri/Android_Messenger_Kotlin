package ru.ok.itmo.example.chats

data class Message(
    val id: Long,
    val from: String,
    val to: String,
    val data: Data,
    val time: Long
)
