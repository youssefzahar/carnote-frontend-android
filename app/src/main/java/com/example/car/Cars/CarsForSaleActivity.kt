package com.example.car.Cars

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.car.Api.RetrofitClient
import com.example.car.Models.Car
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarsForSaleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars_for_sale)
      /*  val recyclerView = findViewById<RecyclerView>(R.id.carsforsale_recycler_view)
            RetrofitClient.carinstace.CarsForSale().enqueue(object : Callback<MutableList<Car>>{
                override fun onResponse(
                    call: Call<MutableList<Car>>,
                    response: Response<MutableList<Car>>
                ) { if(response.isSuccessful){
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(this@CarsForSaleActivity)
                        adapter = CarAdampter(response.body()!!)
                    }

                    }
                }

                override fun onFailure(call: Call<MutableList<Car>>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("error", t.message.toString())

                }

            })*/
    }
}