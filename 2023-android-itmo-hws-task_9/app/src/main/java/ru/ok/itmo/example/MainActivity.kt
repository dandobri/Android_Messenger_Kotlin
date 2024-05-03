package ru.ok.itmo.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        val autorize = Autorize()
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
        .add(R.id.container, autorize).commit()
    }
}