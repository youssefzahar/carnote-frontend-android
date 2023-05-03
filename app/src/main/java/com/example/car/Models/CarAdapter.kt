package com.example.car.Models


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.car.R

class CarAdapter(var cars: List<Car>) :
    RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.carsforsale_layout, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]
        holder.bind(car)
    }

    override fun getItemCount() = cars.size

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvModele: TextView = itemView.findViewById(R.id.showModel)
        private val tvMarque: TextView = itemView.findViewById(R.id.showMarque)
        private val tvDescription: TextView = itemView.findViewById(R.id.showDescription)
        private val tvCarburant: TextView = itemView.findViewById(R.id.showCarburant)
        private val tv_id: TextView = itemView.findViewById(R.id.showid)



        fun bind(car: Car) {
            tvModele.text = car.modele
            tvMarque.text = car.marque
            tvDescription.text = car.description
            tvCarburant.text = car.carburant
            tv_id.text = car._id

        }
    }
}