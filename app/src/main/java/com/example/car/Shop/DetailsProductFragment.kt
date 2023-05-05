package com.example.car.Shop

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Cars.ModifyCarFragment
import com.example.car.Models.Car
import com.example.car.Models.CarResponse
import com.example.car.Models.Product
import com.example.car.Models.ProductResponse
import com.example.car.R
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailsProductFragment : Fragment() {


    private lateinit var product: Product
    private lateinit var titleProduct: TextView
    private lateinit var stockProduct: TextView
    private lateinit var prixProduct: TextView
    private lateinit var descriptionProduct: TextView
    private lateinit var idproduct: TextView
    private lateinit var ProductImage: ImageView


    private lateinit var updateBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        product = arguments?.getParcelable<Product>("product")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details_product, container, false)

        updateBtn = view.findViewById(R.id.updateProductBtn)
        deleteBtn = view.findViewById(R.id.deleteProductBtn)

        updateBtn.setOnClickListener{
            println("to update")
            val fragment = ModifyProductFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }


        deleteBtn.setOnClickListener {
            println("in delete button")
            RetrofitClient.productinstace.DeleteProduct(product._id)
                .enqueue(object: Callback<ProductResponse> {
                    override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                        if(response.code() == 200)
                        {
                         //   findNavController().navigate(R.id.product_item_recycler_view)
                            /*SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Welcome To CarNote")
                                .show();*/
                            val fragment = ShopFragment()
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.frameLayout, fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()

                        } else if (response.code() == 500)
                            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Error !")
                                .setConfirmText("OK!")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()
                    }

                    override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message , Toast.LENGTH_LONG).show()
                    }
                })
        }


        return view
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the TextViews
        ProductImage = view.findViewById(R.id.productImage)

        titleProduct = view.findViewById(R.id.title)
        stockProduct = view.findViewById(R.id.stock)
        prixProduct = view.findViewById(R.id.prix)
        descriptionProduct = view.findViewById(R.id.description)
        idproduct = view.findViewById(R.id.idproduct)

        idproduct.visibility = View.GONE

        // Set the text of the TextViews with the car data
        Picasso.get().load(RetrofitClient.URL + "img/" + product.image).into(ProductImage)


        titleProduct.text = product.title
        stockProduct.text = product.stock.toString()
        prixProduct.text = product.prix.toString()
        descriptionProduct.text = product.description
        idproduct.text = product._id
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefsProduct", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("productId", idproduct.text.toString())
        editor.apply()

    }

}
