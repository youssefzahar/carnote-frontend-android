package com.example.car.Shop

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
import com.example.car.Cars.CarFragment
import com.example.car.Models.CarResponse
import com.example.car.Models.ProductResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ModifyProductFragment : Fragment() {

    private lateinit var descriptionInput: EditText
    private lateinit var stockInput: EditText
    private lateinit var prixInput: EditText
    private lateinit var updateProductButton: Button
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_modify_product, container, false)
        descriptionInput = rootView.findViewById(R.id.modifyproductdescription)
        stockInput = rootView.findViewById(R.id.modifyproductstock)
        prixInput = rootView.findViewById(R.id.modifyproductprix)
        updateProductButton = rootView.findViewById(R.id.modifyproductbutton)


        updateProductButton.setOnClickListener{
            updateProduct()
        }

        return rootView
    }

    private fun updateProduct() {
        val description = descriptionInput.text.toString().trim()
        val stock = stockInput.text.toString().trim()
        val stockValue = stock.toInt()
        val prix = prixInput.text.toString().trim()
        val prixValue = prix.toInt()


        sharedPreferences = requireActivity().getSharedPreferences("MyPrefsProduct", Context.MODE_PRIVATE)
        val productId = sharedPreferences.getString("productId", "")


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
            if (productId != null) {
                sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("token", null)
                RetrofitClient.productinstace.updateProduct("Bearer $token",productId, stockValue, prixValue, description)
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
                                    .setTitleText("Product Updated Successfully!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                    .show()
                            } else if(response.code() == 402)
                            {
                                SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Not Allowed")
                                    .show();
                            }else {
                                // Show error dialog
                                SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Failed to update Product.")
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

}