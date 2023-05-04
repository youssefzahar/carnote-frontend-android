package com.example.car.Shop

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.car.Api.RetrofitClient
import com.example.car.Cars.AddCarFragment
import com.example.car.Cars.CarFragment
import com.example.car.Models.CarAdapter
import com.example.car.Models.CarResponse
import com.example.car.Models.ProductAdapter
import com.example.car.Models.ProductResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopFragment : Fragment() {
    private lateinit var rvProducts: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    lateinit var sprole: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentmanager = requireFragmentManager()
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        rvProducts = view.findViewById(R.id.product_item_recycler_view)
        rvProducts.layoutManager = LinearLayoutManager(requireContext())
        // carAdapter = CarAdapter(listOf()) // create an empty adapter
        productAdapter = ProductAdapter(listOf(), fragmentmanager)
        rvProducts.adapter = productAdapter

        val sharedPreferences = requireContext().getSharedPreferences("myPrefsRole", Context.MODE_PRIVATE)


        val add_product_btn = view.findViewById<Button>(R.id.add_product_button)
        add_product_btn.setOnClickListener {

        }

        if(sharedPreferences.getString("role", "") == "User") {
            add_product_btn.visibility = View.GONE
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RetrofitClient.productinstace.getAllProducts().enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    println("in success")
                    val products = response.body()?.products
                    if (products != null) {
                        productAdapter.products = products // update the adapter with the retrieved cars
                        productAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                    }
                } else {
                    Log.e(ShopFragment.TAG, "Failed to get cars: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e(ShopFragment.TAG, "Failed to get cars", t)
            }
        })
    }

    companion object {
        private const val TAG = "ShopFragment"
    }

}