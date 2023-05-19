package com.example.car.Shop

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Cars.CarFragment
import com.example.car.Cars.ModifyCarFragment
import com.example.car.Models.Car
import com.example.car.Models.CarAdapter
import com.example.car.Models.CarResponse
import com.example.car.Models.CommentAdapter
import com.example.car.Models.CommentResponse
import com.example.car.Models.Product
import com.example.car.Models.ProductResponse
import com.example.car.Models.UserResponse
import com.example.car.R
import com.example.car.UserActivities.UpdateUserFragment
import com.example.car.databinding.FragmentDetailsProductBinding
import com.example.car.databinding.FragmentProfileBinding
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
    private lateinit var descriptionInput: EditText

    private lateinit var updateBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var commentbtn : Button
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: FragmentDetailsProductBinding

    private lateinit var rvCars: RecyclerView
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = arguments?.getParcelable<Product>("product")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsProductBinding.inflate(inflater, container, false)
        val view = binding.root

        rvCars = binding.commentRecyclerView
        rvCars.layoutManager = LinearLayoutManager(requireContext())
        commentAdapter = CommentAdapter(listOf(), requireFragmentManager())
        rvCars.adapter = commentAdapter

        updateBtn = view.findViewById(R.id.updateProductBtn)
        deleteBtn = view.findViewById(R.id.deleteProductBtn)
        commentbtn = view.findViewById(R.id.add_comment_button)
        descriptionInput = view.findViewById(R.id.comment_text)

        updateBtn.setOnClickListener{
            println("to update")
            val fragment = ModifyProductFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        commentbtn.setOnClickListener{
            addComment()
        }

        deleteBtn.setOnClickListener {
            println("in delete button")
            RetrofitClient.productinstace.DeleteProduct(product._id)
                .enqueue(object: Callback<ProductResponse> {
                    override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                        if(response.code() == 200)
                        {
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

        // Retrieve comments for the product
        RetrofitClient.commentinstace.getUsersComments(product._id).enqueue(object : Callback<CommentResponse> {
            override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                if (response.isSuccessful) {
                    val comments = response.body()?.comments
                    if (comments != null) {
                        commentAdapter.comments = comments
                        commentAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e(TAG, "Failed to get comments: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                Log.e(TAG, "Failed to get comments", t)
            }
        })


        return view
    }

    private fun addComment() {
        val description = descriptionInput.text.toString().trim()
        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if(description.isEmpty()) {
            descriptionInput.error = " Required"
        }
        else {
            RetrofitClient.commentinstace.AddComment("Bearer $token", product._id , description)
                .enqueue(object: Callback<CommentResponse> {
                    override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                        if(response.code() == 200) {
                            val fragment = ShopFragment()
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.frameLayout, fragment)
                            transaction.addToBackStack(null)
                            transaction.commit()

                            SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Comment added successfully!")
                                .setConfirmText("OK")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()
                        } else {
                            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Failed to add car.")
                                .setConfirmText("OK")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message , Toast.LENGTH_LONG).show()
                    }

                })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ProductImage = view.findViewById(R.id.productImage)
        titleProduct = view.findViewById(R.id.title)
        stockProduct = view.findViewById(R.id.stock)
        prixProduct = view.findViewById(R.id.prix)
        descriptionProduct = view.findViewById(R.id.description)
        idproduct = view.findViewById(R.id.idproduct)

        idproduct.visibility = View.GONE

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
