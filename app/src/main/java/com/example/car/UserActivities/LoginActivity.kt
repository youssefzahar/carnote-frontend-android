package com.example.car.UserActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.car.MainActivity
import com.example.car.R

class LoginActivity : AppCompatActivity() {
    lateinit var forgotpassword : TextView
    lateinit var usernameinput : EditText
    lateinit var passwordinput : EditText
    lateinit var btn_login: Button
    lateinit var textView_register: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
       forgotpassword = findViewById(R.id.forgotpassword)
        usernameinput = findViewById(R.id.username)
        passwordinput = findViewById(R.id.password)
        btn_login = findViewById(R.id.loginbutton)
        textView_register = findViewById(R.id.registerbtn)

        btn_login.setOnClickListener {
            checkcredentials();
        }

        textView_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()

        }
    }


    private fun checkcredentials() {
        val username = usernameinput.text.toString().trim()
        val password = passwordinput.text.toString().trim()

        if(username.isEmpty()){
            usernameinput.error = "Username Required"
        }

        if(password.isEmpty()) {
            passwordinput.error = "Password Required"
        }

        else {
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}