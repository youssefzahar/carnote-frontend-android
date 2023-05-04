package com.example.car.Cars

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.car.Api.RetrofitClient
import com.example.car.Models.Car
import com.example.car.Models.CarAdapter
import com.example.car.Models.CarResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CarFragment : Fragment() {
    private lateinit var rvCars: RecyclerView
    private lateinit var carAdapter: CarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentmanager = requireFragmentManager()
        val view = inflater.inflate(R.layout.fragment_car, container, false)
        rvCars = view.findViewById(R.id.carsforsale_recycler_view)
        rvCars.layoutManager = LinearLayoutManager(requireContext())
       // carAdapter = CarAdapter(listOf()) // create an empty adapter
        carAdapter = CarAdapter(listOf(), fragmentmanager)
        rvCars.adapter = carAdapter
       /* carAdapter.onItemClick = {
            val intent = Intent(this, CarDetailsFragment::class.java)
            intent.putE
        }*/
        val add_car_btn = view.findViewById<Button>(R.id.add_car_button)
        add_car_btn.setOnClickListener {
            val fragment = AddCarFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RetrofitClient.carinstace.getAllCars().enqueue(object : Callback<CarResponse> {
            override fun onResponse(call: Call<CarResponse>, response: Response<CarResponse>) {
                if (response.isSuccessful) {
                    val cars = response.body()?.cars
                    if (cars != null) {
                        carAdapter.cars = cars // update the adapter with the retrieved cars
                        carAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                    }
                } else {
                    Log.e(TAG, "Failed to get cars: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CarResponse>, t: Throwable) {
                Log.e(TAG, "Failed to get cars", t)
            }
        })
    }

    companion object {
        const val TAG = "CarsFragment"
    }
}
