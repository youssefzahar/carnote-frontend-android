package com.example.car.Models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.car.R

class ProductApater (var products: List<Product>) :
    RecyclerView.Adapter<ProductApater.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.carsforsale_layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount() = products.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      /*  private val tvModele: TextView = itemView.findViewById(R.id.showModel)
        private val tvMarque: TextView = itemView.findViewById(R.id.showMarque)
        private val tvDescription: TextView = itemView.findViewById(R.id.showDescription)
        private val tvCarburant: TextView = itemView.findViewById(R.id.showCarburant)
        private val tv_id: TextView = itemView.findViewById(R.id.showid)*/



        fun bind(product: Product) {
         /*   tvModele.text = car.modele
            tvMarque.text = car.marque
            tvDescription.text = car.description
            tvCarburant.text = car.carburant
            tv_id.text = car._id*/

        }
    }
}