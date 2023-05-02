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
import com.example.car.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var btn_to_update: Button




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        /*btn_to_update = view.findViewById(R.id.accountsettingsbutton)
        btn_to_update.setOnClickListener {
            val intent = Intent(activity, UpdateUserActivity::class.java)
            startActivity(intent)
        }*/

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.accountsettingsbutton.setOnClickListener {
            val intent = Intent(activity, UpdateUserActivity::class.java)
            startActivity(intent)
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
                        println("aaaaaaaaaaaaa")
                        println(RetrofitClient.URL+"img/"+user?.image)
                        //  Glide.with(requireContext()).load(user?.userimage).placeholder(R.drawable.ic_person).into(binding.userimage)
                    } else {
                        Toast.makeText(requireContext(), "Unable to get user info", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return binding.root
        //return view
    }

}