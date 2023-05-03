package com.example.car.Shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.car.R

class ProductsForSaleActivity : AppCompatActivity() {
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