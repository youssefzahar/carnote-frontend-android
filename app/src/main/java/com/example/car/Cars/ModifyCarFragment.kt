package com.example.car.Cars

import android.content.Context
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
import com.example.car.Models.CarResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ModifyCarFragment : Fragment() {

    private lateinit var descriptionInput: EditText
    private lateinit var circulationDateInput: EditText
    private lateinit var updateCarButton: Button
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_modify_car, container, false)
        descriptionInput = rootView.findViewById(R.id.description)
        circulationDateInput = rootView.findViewById(R.id.circulation_date)
        updateCarButton = rootView.findViewById(R.id.modifycarbutton)


        updateCarButton.setOnClickListener{
            updateCar()
        }

        return rootView
    }

    private fun updateCar() {
        val description = descriptionInput.text.toString().trim()
        val circulationDate = circulationDateInput.text.toString().trim()

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefsCar", Context.MODE_PRIVATE)
        val carId = sharedPreferences.getString("carId", "")


        if(description.isEmpty()) {
            descriptionInput.error = " Required"
        }
        if(circulationDate.isEmpty()) {
            circulationDateInput.error = " Required"
        } else {
            if (carId != null) {
                RetrofitClient.carinstace.updateCar(carId, description, circulationDate)
                    .enqueue(object: Callback<CarResponse> {
                        override fun onResponse(call: Call<CarResponse>, response: Response<CarResponse>) {
                            if(response.code() == 200) {
                                // Refresh the car list on the parent activity
                                val fragment = CarsFragment()
                                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.frameLayout, fragment)
                                transaction.addToBackStack(null)
                                transaction.commit()

                                // Show success dialog
                                SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Car Updated Successfully!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                    .show()
                            } else {
                                // Show error dialog
                                SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Failed to update car.")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<CarResponse>, t: Throwable) {
                            Toast.makeText(requireContext(), t.message , Toast.LENGTH_LONG).show()
                        }

                    })
            }
        }
    }

}