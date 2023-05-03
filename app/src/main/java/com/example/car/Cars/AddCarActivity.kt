package com.example.car.Cars

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Models.CarResponse
import com.example.car.Models.UserResponse
import com.example.car.R
import com.example.car.UserActivities.LoginActivity
import com.example.car.UserActivities.ProfileFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddCarActivity : AppCompatActivity() {

    lateinit var modelinput : EditText
    lateinit var marqueinput : EditText
    lateinit var puissanceinput : EditText
    lateinit var carburantinput : EditText
    lateinit var descriptioninput : EditText
    lateinit var circulation_dateinput : EditText
    lateinit var addcarbtn: Button
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        modelinput = findViewById(R.id.model)
        marqueinput = findViewById(R.id.marque)
        puissanceinput = findViewById(R.id.puissance)
        carburantinput = findViewById(R.id.carburant)
        descriptioninput = findViewById(R.id.description)
        circulation_dateinput = findViewById(R.id.circulation_date)
        addcarbtn = findViewById(R.id.addcarbutton)

        addcarbtn.setOnClickListener{
            checkcredentials();
        }
    }

    private fun checkcredentials() {
        val intentCars = Intent(this, CarsFragment::class.java)
        val model = modelinput.text.toString().trim()
        val marque = marqueinput.text.toString().trim()
        val puissance = puissanceinput.text.toString().trim()
        val puissanceValue = puissance.toInt()
        val carburant = carburantinput.text.toString().trim()
        val description = descriptioninput.text.toString().trim()
        val circulation_date = circulation_dateinput.text.toString().trim()
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        //  val role = roleinput.text.toString().trim()

/*
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.FRANCE)
        val date = inputFormat.parse(circulation_date)
        val formattedDate = outputFormat.format(date)*/

        if(model.isEmpty()){
            modelinput.error = " Required"
        }
        //if(email.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
        if(marque.isEmpty()) {
            marqueinput.error = " Required"
        }
        if(puissance.isEmpty()) {
            puissanceinput.error = " Required"
        }
        if(carburant.isEmpty()){
            carburantinput.error = " Required"
        }
        //if(email.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
        if(description.isEmpty()) {
            descriptioninput.error = " Required"
        }
        if(circulation_date.isEmpty()) {
            circulation_dateinput.error = " Required"
        }


        else {
            RetrofitClient.carinstace.AddCar("Bearer $token", model,marque, puissanceValue ,carburant, description, circulation_date)
                .enqueue(object: Callback<CarResponse> {
                    override fun onResponse(call: Call<CarResponse>, response: Response<CarResponse>) {
                        if(response.code() == 200)
                        {
                            startActivity(intentCars)
                            /*SweetAlertDialog(this@RegisterActivity, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Welcome To CarNote")
                                .show();  */                      }
                        else if(response.code()==500)
                        //Toast.makeText(applicationContext, "user exists", Toast.LENGTH_LONG).show()

                            SweetAlertDialog(this@AddCarActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Error !")
                                .setConfirmText("OK!")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()
                    }

                    override fun onFailure(call: Call<CarResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message , Toast.LENGTH_LONG).show()
                    }

                })
        }

    }
}