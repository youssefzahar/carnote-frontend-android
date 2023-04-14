package com.example.car.Cars

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.car.Api.CarService
import com.example.car.Models.CarAdapter
import com.example.car.Models.CarResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyCarsFragment : Fragment() {

    private lateinit var rvCars: RecyclerView
    private lateinit var carAdapter: CarAdapter
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cars, container, false)
        rvCars = view.findViewById(R.id.carsforsale_recycler_view)
        //rvCars.layoutManager = LinearLayoutManager(requireContext())
        rvCars.layoutManager = GridLayoutManager(requireContext(),2)
        carAdapter = CarAdapter(listOf()) // create an empty adapter
        rvCars.adapter = carAdapter
        val add_car_btn = view.findViewById<Button>(R.id.add_car_button)
        add_car_btn.setOnClickListener {
            val intentAddCar = Intent(activity, AddCarActivity::class.java)
            startActivity(intentAddCar)
        }
        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // make API call to get the list of cars
        val service = Retrofit.Builder()
            .baseUrl("http://192.168.1.18:9090/") // replace with your API endpoint
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CarService::class.java)

        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        service.UserCars("Bearer $token").enqueue(object : Callback<CarResponse> {
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
        private const val TAG = "CarsFragment"
    }
}