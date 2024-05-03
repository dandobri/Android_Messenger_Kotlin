package ru.ok.itmo.example.logins

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.ok.itmo.example.chats.Message

interface ChatApi {
    @GET("/channel/{chatName}")
    fun getChat(@Path("chatName") chatName: String): Call<List<Message>>

    @GET("/inbox/{userName}")
    fun getInbox(@Path("userName") userName: String): Call<List<Message>>

    @GET("/channels")
    fun getChats(): Call<List<String>>

    @GET("/img/{path}")
    fun getImage(@Path("path") path: String): Call<String>

    @POST("/channel/{chatName}")
    fun createChat(@Path("chatName") chatName: String, @Body message: Message): Call<Long>
    companion object {
        fun create(retrofit: Retrofit): ChatApi {
            return retrofit.create(ChatApi::class.java)
        }
    }
}