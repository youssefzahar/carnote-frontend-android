package com.example.car.Models

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.car.Api.RetrofitClient
import com.example.car.Cars.CarDetailsFragment
import com.example.car.R
import com.example.car.databinding.CarsforsaleLayoutBinding
import com.example.car.databinding.CommentItemBinding
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter(var comments: List<Comment>, val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    //  var onItemClick : ((Car) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        /*val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.carsforsale_layout, parent, false)*/
        val view = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]



        // Picasso.get().load(car.image).into(holder.imageView)
        //holder.bind(car)

        /* holder.imageView.setOnClickListener{
             onItemClick?.invoke(car)
         }*/
        val txDescription = holder.itemView.findViewById<TextView>(R.id.description_textview)
        val tvUserName = holder.itemView.findViewById<TextView>(R.id.commentor_name)

        txDescription.text = comment.description
        tvUserName.text = comment.username
        Glide.with(holder.itemView.context).load(RetrofitClient.URL+"img/"+comment.userimage).into(holder.itemView.findViewById<CircleImageView>(R.id.commentor_image))


    }

    override fun getItemCount() = comments.size


    inner class CommentViewHolder(view: CommentItemBinding) : RecyclerView.ViewHolder(view.root) {
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
}