package com.example.car.Shop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.car.Cars.AddCarActivity
import com.example.car.Models.CarAdapter
import com.example.car.R

class ProductsFragment : Fragment() {
    private lateinit var rvProducts: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_products, container, false)
        val add_product_btn = view?.findViewById<Button>(R.id.add_product_button)
        add_product_btn?.setOnClickListener {
            val intentAddProduct = Intent(activity, addProductActivity::class.java)
            startActivity(intentAddProduct)
        }
        return view
    }


    companion object {
        private const val TAG = "ProductsFragment"
    }

}


