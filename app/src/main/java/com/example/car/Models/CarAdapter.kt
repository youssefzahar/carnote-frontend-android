package com.example.car.Models


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.car.Api.RetrofitClient
import com.example.car.Cars.CarDetailsFragment
import com.example.car.R
import com.example.car.databinding.CarsforsaleLayoutBinding
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CarAdapter(var cars: List<Car>, val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

  //  var onItemClick : ((Car) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        /*val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.carsforsale_layout, parent, false)*/
        val view = CarsforsaleLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]
       // Picasso.get().load(car.image).into(holder.imageView)
        //holder.bind(car)

       /* holder.imageView.setOnClickListener{
            onItemClick?.invoke(car)
        }*/
        val tvModele = holder.itemView.findViewById<TextView>(R.id.showModel)
        val tvMarque = holder.itemView.findViewById<TextView>(R.id.showMarque)
        val tvDescription = holder.itemView.findViewById<TextView>(R.id.showDescription)
        val tvId = holder.itemView.findViewById<TextView>(R.id.showId)
        val tvCirculationdate = holder.itemView.findViewById<TextView>(R.id.showCirculationdate)
        val tvCarburant = holder.itemView.findViewById<TextView>(R.id.showCarburant)
        val tvPuissance = holder.itemView.findViewById<TextView>(R.id.showPuissance)
        tvModele.text = car.modele
        tvMarque.text = car.marque
        tvDescription.text = car.description
        tvId.text = car._id
        tvCirculationdate.text = car.date_circulation
        tvCarburant.text = car.carburant
        tvPuissance.text = car.puissance
        tvPuissance.visibility = View.GONE
        tvId.visibility = View.GONE
        tvCirculationdate.visibility = View.GONE
        tvDescription.visibility = View.GONE
        tvCarburant.visibility = View.GONE

        Glide.with(holder.itemView.context).load(RetrofitClient.URL+"img/"+car.image).into(holder.itemView.findViewById<CircleImageView>(R.id.carImage))
       // Picasso.get().load(RetrofitClient.URL+"img/"+car.image).into(imageView)
        holder.itemView.setOnClickListener{
            navigateToCarDetails(car, fragmentManager)
        }
    }

    override fun getItemCount() = cars.size


    inner class CarViewHolder(view: CarsforsaleLayoutBinding) : RecyclerView.ViewHolder(view.root) {
     /*   private val tvModele: TextView = itemView.findViewById(R.id.showModel)
        private val tvMarque: TextView = itemView.findViewById(R.id.showMarque)
        val imageView: ImageView = itemView.findViewById(R.id.carImage)
        //private val tvDescription: TextView = itemView.findViewById(R.id.showDescription)
        //private val tvCarburant: TextView = itemView.findViewById(R.id.showCarburant)
        //private val tv_id: TextView = itemView.findViewById(R.id.showid)

        init {
            itemView.setOnClickListener {
                val car = itemView.tag as Car
                val bundle = Bundle().apply {
                    putParcelable("car", car)
                }
                val carDetailsFragment = CarDetailsFragment().apply {
                    arguments = bundle
                }
                (itemView.context as AppCompatActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.details_fragment_container, carDetailsFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }




        fun bind(car: Car) {
            tvModele.text = car.modele
            tvMarque.text = car.marque
            Picasso.get().load(RetrofitClient.URL+"img/"+car.image).into(imageView)
            itemView.tag = car


            //  tvDescription.text = car.description
            //tvCarburant.text = car.carburant
            //tv_id.text = car._id

        }*/
    }

    private fun navigateToCarDetails(car: Car, fragmentManager: FragmentManager) {
        val bundle = Bundle().apply {
            putParcelable("car", car)
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