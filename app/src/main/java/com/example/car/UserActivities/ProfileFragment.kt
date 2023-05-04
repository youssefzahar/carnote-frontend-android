package com.example.car.UserActivities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.car.Api.RetrofitClient
import com.example.car.Models.UserResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.SharedPreferences
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.car.Cars.CarFragment
import com.example.car.Models.CarAdapter
import com.example.car.Models.CarResponse
import com.example.car.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var btn_to_update: Button

    private lateinit var rvCars: RecyclerView
    private lateinit var carAdapter: CarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        rvCars = binding.carsforsaleRecyclerView
        rvCars.layoutManager = LinearLayoutManager(requireContext())
        carAdapter = CarAdapter(listOf(), requireFragmentManager())
        rvCars.adapter = carAdapter

        binding.accountsettingsbutton.setOnClickListener {
            val fragment = UpdateUserFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if (token != null) {
            RetrofitClient.instance.getUser("Bearer $token").enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        val user = response.body()?.user
                        binding.username.text = user?.username
                        binding.email.text = user?.email
                        Picasso.get().load(RetrofitClient.URL+"img/"+user?.image).into(binding.userimage)
                    } else {
                        Toast.makeText(requireContext(), "Unable to get user info", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show()
                }
            })

            RetrofitClient.carinstace.UserCars("Bearer $token").enqueue(object : Callback<CarResponse> {
                override fun onResponse(call: Call<CarResponse>, response: Response<CarResponse>) {
                    if (response.isSuccessful) {
                        val cars = response.body()?.cars
                        if (cars != null) {
                            carAdapter.cars = cars
                            carAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Log.e(CarFragment.TAG, "Failed to get cars: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CarResponse>, t: Throwable) {
                    Log.e(CarFragment.TAG, "Failed to get cars", t)
                }
            })
        }

        return binding.root
    }
}