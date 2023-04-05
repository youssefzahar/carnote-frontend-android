package com.example.car.UserActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.car.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    lateinit var logintextview : TextView
    lateinit var usernameinput : EditText
    lateinit var emailinput : EditText
    lateinit var passwordinput : EditText
    lateinit var roleinput : EditText
    lateinit var btnregister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        logintextview = findViewById(R.id.logintextview)
        usernameinput = findViewById(R.id.username)
        emailinput = findViewById(R.id.email)
        passwordinput = findViewById(R.id.password)
        roleinput = findViewById(R.id.role)
        btnregister = findViewById(R.id.registerbutton)

        btnregister.setOnClickListener{
            checkcredentials();
        }

        logintextview.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkcredentials() {
        val username = usernameinput.text.toString().trim()
        val email = emailinput.text.toString().trim()
        val password = passwordinput.text.toString().trim()
        val role = roleinput.text.toString().trim()

        if(username.isEmpty()){
            usernameinput.error = "Username Required"
        }
        //if(email.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
        if(email.isEmpty()) {
            emailinput.error = "Email Required"
        }
        if(password.isEmpty()) {
            passwordinput.error = "Password Required"
        }
        if(role.isEmpty()) {
            roleinput.error = "Role Required"
        }
        else {
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
        }

    }
}