package com.example.car.UserActivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Models.UserResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdateUserFragment : Fragment() {

    private lateinit var emailinput : EditText
    private lateinit var passwordinput : EditText
    private lateinit var btn_update: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_user, container, false)

        emailinput = view.findViewById(R.id.email)
        passwordinput = view.findViewById(R.id.password)
        btn_update = view.findViewById(R.id.updatebutton)

        btn_update.setOnClickListener {
            checkcredentials();
        }

        return view
    }

    private fun checkcredentials() {
        val intentProfile = Intent(activity, ProfileFragment::class.java)
        val email = emailinput.text.toString().trim()
        val password = passwordinput.text.toString().trim()
        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if(email.isEmpty()){
            emailinput.error = "Email Required"
        }

        if(password.isEmpty()) {
            passwordinput.error = "Password Required"
        }

        else {
            RetrofitClient.instance.updateuser("Bearer $token",email,password)
                .enqueue(object: Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if(response.code() == 200)
                        {
                            startActivity(intentProfile)
                            //Toast.makeText(requireContext(), "new user", Toast.LENGTH_LONG).show()
                            SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("User Updated Successfully")
                                .show();
                        }
                        else if(response.code()==500)
                        {
                            //Toast.makeText(requireContext(), "error", Toast.LENGTH_LONG).show()
                            SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("An Error Has Occured")
                                //.setContentText("You Submitted The Wrong Code!")
                                .show()

                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message , Toast.LENGTH_LONG).show()
                    }

                })
        }
    }
}