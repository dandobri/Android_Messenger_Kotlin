package ru.ok.itmo.example

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ok.itmo.example.login.Login
import ru.ok.itmo.example.login.LoginApi
import ru.ok.itmo.example.login.LoginRequest
import ru.ok.itmo.example.login.RetrofitLogin
import ru.ok.itmo.example.logins.ChatApi
import kotlin.math.log

class Autorize : Fragment(R.layout.autorize) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val login = view.findViewById<EditText>(R.id.login_text)
        val password = view.findViewById<EditText>(R.id.password_text)
        val enter = view.findViewById<Button>(R.id.enter_button)
        fun toast(message: String) = Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
        login.addTextChangedListener {
            enter.isEnabled = !(login.text.isEmpty() || password.text.isEmpty())
        }
        password.addTextChangedListener {
            enter.isEnabled = !(login.text.isEmpty() || password.text.isEmpty())
        }
        val retrofit = RetrofitLogin().login()
        val loginApi = LoginApi.create(retrofit)
        enter.setOnClickListener {
            loginApi.login(Login(login.text.toString(), password.text.toString())).enqueue(object : Callback<String> {
                override fun onResponse(p0: Call<String>, p1: Response<String>) {
                    if(p1.body() == null) {
                        toast("you can not authorize")
                    }
                }
                override fun onFailure(p0: Call<String>, p1: Throwable) {
                    val home = Home(retrofit)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, home).addToBackStack(null).commit()
                }
            })
        }
    }
}
