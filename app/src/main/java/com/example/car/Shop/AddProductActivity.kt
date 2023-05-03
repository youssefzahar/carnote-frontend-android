package com.example.car.Shop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Cars.CarsFragment
import com.example.car.Models.CarResponse
import com.example.car.Models.ProductResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductActivity : AppCompatActivity() {

    lateinit var titleinput : EditText
    lateinit var stockinput : EditText
    lateinit var prixinput : EditText
    lateinit var descriptioninput : EditText
    lateinit var addproductbtn: Button
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        titleinput = findViewById(R.id.title)
        stockinput = findViewById(R.id.stock)
        prixinput = findViewById(R.id.prix)
        descriptioninput = findViewById(R.id.description)
        addproductbtn = findViewById(R.id.addproductbutton)

        addproductbtn.setOnClickListener {
            checkcredentials();
        }
    }

        private fun checkcredentials() {
            val intentProducts = Intent(this, ProductsFragment::class.java)
            val title = titleinput.text.toString().trim()
            val stock = stockinput.text.toString().trim()
            val prix =prixinput.text.toString().trim()
            val prixValue =prix.toInt()
            val description = descriptioninput.text.toString().trim()

            sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)


            if(title.isEmpty()){
                titleinput.error = " Required"
            }
            //if(email.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
            if(stock.isEmpty()) {
                stockinput.error = " Required"
            }
            if(prix.isEmpty()) {
                prixinput.error = " Required"
            }
            if(description.isEmpty()){
                descriptioninput.error = " Required"
            }


            else {
                RetrofitClient.productinstace.AddProduct("Bearer $token", title,stock, prixValue ,description)
                    .enqueue(object: Callback<ProductResponse> {
                        override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                            if(response.code() == 200)
                            {
                                startActivity(intentProducts)
                                /*SweetAlertDialog(this@RegisterActivity, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Welcome To CarNote")
                                    .show();  */                      }
                            else if(response.code()==500)
                            //Toast.makeText(applicationContext, "user exists", Toast.LENGTH_LONG).show()

                                SweetAlertDialog(this@AddProductActivity, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Error !")
                                    .setConfirmText("OK!")
                                    .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                    .show()
                        }

                        override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message , Toast.LENGTH_LONG).show()
                        }

                    })
            }


        }

        }



