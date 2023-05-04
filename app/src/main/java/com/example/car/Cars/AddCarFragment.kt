package com.example.car.Cars

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.MainActivity
import com.example.car.Models.CarResponse
import com.example.car.R
import com.example.car.UserActivities.UpdateUserFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ArrayAdapter


class AddCarFragment : Fragment() {

    private lateinit var modelInput: Spinner
    private lateinit var marqueInput: EditText
    private lateinit var puissanceInput: EditText
    private lateinit var carburantInput: Spinner
    private lateinit var descriptionInput: EditText
    private lateinit var circulationDateInput: EditText
    private lateinit var addCarButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_car, container, false)

        modelInput = rootView.findViewById(R.id.model)
        marqueInput = rootView.findViewById(R.id.marque)
        puissanceInput = rootView.findViewById(R.id.puissance)
        carburantInput = rootView.findViewById(R.id.carburant)
        descriptionInput = rootView.findViewById(R.id.description)
        circulationDateInput = rootView.findViewById(R.id.circulation_date)
        addCarButton = rootView.findViewById(R.id.addcarbutton)


        val models  = arrayOf("BMW","Ferrari","Huandai")
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, models)
        modelInput.adapter = adapter

        modelInput.setOnItemSelectedListener(object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem  = models[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })

        val carburants  = arrayOf("Essence","Gazoil")
        val adaptercarburants = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, carburants)
        carburantInput.adapter = adaptercarburants

        carburantInput.setOnItemSelectedListener(object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem  = models[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })

        addCarButton.setOnClickListener{
            checkCredentials()
        }

        return rootView
    }

    private fun checkCredentials() {
        val model = modelInput.selectedItem.toString().trim()
        val marque = marqueInput.text.toString().trim()
        val puissance = puissanceInput.text.toString().trim()
        val puissanceValue = puissance.toInt()
        val carburant = carburantInput.selectedItem.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val circulationDate = circulationDateInput.text.toString().trim()
        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if(marque.isEmpty()) {
            marqueInput.error = " Required"
        }
        if(puissance.isEmpty()) {
            puissanceInput.error = " Required"
        }
        if(description.isEmpty()) {
            descriptionInput.error = " Required"
        }
        if(circulationDate.isEmpty()) {
            circulationDateInput.error = " Required"
        } else {
            RetrofitClient.carinstace.AddCar("Bearer $token", model,marque, puissanceValue ,carburant, description, circulationDate)
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
                                .setTitleText("Car added successfully!")
                                .setConfirmText("OK")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()
                        } else {
                            // Show error dialog
                            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Failed to add car.")
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