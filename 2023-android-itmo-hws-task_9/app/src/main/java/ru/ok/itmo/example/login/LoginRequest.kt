package ru.ok.itmo.example.login

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import ru.ok.itmo.example.login.Login

class LoginRequest(val loginApi: LoginApi, val loginText: String, val passwordText: String) {
    fun login(): Call<String> {
        return loginApi.login(
            Login(
                loginText,
                passwordText
            )
        )
    }
    fun logout(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            loginApi.logout(token)
        }
    }
}