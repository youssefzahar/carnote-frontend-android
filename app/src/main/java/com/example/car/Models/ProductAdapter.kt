package com.example.car.Models

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.car.Api.RetrofitClient
import com.example.car.Cars.CarDetailsFragment
import com.example.car.R
import com.example.car.databinding.CarsforsaleLayoutBinding
import com.example.car.databinding.ProductItemLayoutBinding
import de.hdodenhof.circleimageview.CircleImageView

class ProductAdapter(var products: List<Product>, val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        /*val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.carsforsale_layout, parent, false)*/
        val view = ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        // Picasso.get().load(car.image).into(holder.imageView)
        //holder.bind(car)

        /* holder.imageView.setOnClickListener{
             onItemClick?.invoke(car)
         }*/
        val tvtitle = holder.itemView.findViewById<TextView>(R.id.productTitle)
        val tvstock = holder.itemView.findViewById<TextView>(R.id.productStock)
        val tvprix = holder.itemView.findViewById<TextView>(R.id.productPrix)
        val tvId = holder.itemView.findViewById<TextView>(R.id.productId)
        val tvdescription = holder.itemView.findViewById<TextView>(R.id.productDescription)
        val tvownedby = holder.itemView.findViewById<TextView>(R.id.productOwner)
        tvtitle.text = product.title
        tvstock.text = product.stock.toString()
        tvprix.text = product.prix.toString()
        tvId.text = product._id
        tvdescription.text = product.description
        tvownedby.text = product.owned_by
        tvId.visibility = View.GONE

        Glide.with(holder.itemView.context).load(RetrofitClient.URL+"img/"+product.image).into(holder.itemView.findViewById<CircleImageView>(
            R.id.productImage))
        // Picasso.get().load(RetrofitClient.URL+"img/"+car.image).into(imageView)
        holder.itemView.setOnClickListener{
            navigateToCarDetails(product, fragmentManager)
        }
    }

    override fun getItemCount() = products.size


    inner class ProductViewHolder(view: ProductItemLayoutBinding) : RecyclerView.ViewHolder(view.root) {}

    private fun navigateToCarDetails(product: Product, fragmentManager: FragmentManager) {
        val bundle = Bundle().apply {
            putParcelable("product", product)
        }
        val carDetailsFragment = CarDetailsFragment()
        carDetailsFragment.arguments = bundle
        fragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, carDetailsFragment)
            addToBackStack(null)
            commit()
        }
    }


}