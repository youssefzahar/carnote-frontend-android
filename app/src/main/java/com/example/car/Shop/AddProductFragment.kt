package com.example.car.Shop

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
import com.example.car.Cars.CarFragment
import com.example.car.Models.CarResponse
import com.example.car.Models.ProductResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddProductFragment : Fragment() {

    private lateinit var titleInput: EditText
    private lateinit var stockInput: EditText
    private lateinit var prixInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var addProductButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_product, container, false)

        titleInput = rootView.findViewById(R.id.producttitle)
        stockInput = rootView.findViewById(R.id.productstock)
        prixInput = rootView.findViewById(R.id.productprix)
        descriptionInput = rootView.findViewById(R.id.productdescription)
        addProductButton = rootView.findViewById(R.id.addproductbutton)


        addProductButton.setOnClickListener{
            checkCredentials()
        }

        return rootView
    }

    private fun checkCredentials() {
        val title = titleInput.text.toString().trim()
        val stock = stockInput.text.toString().trim()
        val stockValue = stock.toInt()
        val prix = prixInput.text.toString().trim()
        val prixValue = prix.toInt()
        val description = descriptionInput.text.toString().trim()
        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if(title.isEmpty()) {
            titleInput.error = " Required"
        }
        if(stock.isEmpty()) {
            stockInput.error = " Required"
        }
        if(prix.isEmpty()) {
            prixInput.error = " Required"
        }
        if(description.isEmpty()) {
            descriptionInput.error = " Required"
        }
        else {
            RetrofitClient.productinstace.AddCar("Bearer $token", title,stockValue, prixValue , description)
                .enqueue(object: Callback<ProductResponse> {
                    override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                        if(response.code() == 200) {
                            // Refresh the car list on the parent activity
                            val fragment = ShopFragment()
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

                    override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message , Toast.LENGTH_LONG).show()
                    }

                })
        }
    }
}