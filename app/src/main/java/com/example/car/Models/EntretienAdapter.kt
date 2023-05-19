package com.example.car.Models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.car.R


class EntretienAdapter(var entretien: List<Entretien>) :
    RecyclerView.Adapter<EntretienAdapter.EntretienViewHolder>() {

    inner class EntretienViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvtitle: TextView = itemView.findViewById(R.id.title)
        val tvdescription: TextView = itemView.findViewById(R.id.description)
        val tvdate: TextView = itemView.findViewById(R.id.date)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)

        fun collapseExpandedView(){
            tvdate.visibility = View.GONE
        }
    }

    fun setFilteredList(entretien: List<Entretien>) {
        this.entretien = entretien
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntretienViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.eaxh_item, parent, false)
        return EntretienViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntretienViewHolder, position: Int) {

        val entretien1 = entretien[position]
        holder.tvtitle.text = entretien1.title
        holder.tvdescription.text = entretien1.description
        holder.tvdate.text = entretien1.date


        val isExpandable: Boolean = entretien1.isExpandable
        holder.tvdescription.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.constraintLayout.setOnClickListener {
            isAnyItemExpanded(position)
            entretien1.isExpandable = !entretien1.isExpandable
            notifyItemChanged(position , Unit)
        }

    }

    private fun isAnyItemExpanded(position: Int){
        val temp = entretien.indexOfFirst {
            it.isExpandable
        }
        if (temp >= 0 && temp != position){
            entretien[temp].isExpandable = false
            notifyItemChanged(temp , 0)
        }
    }

    override fun onBindViewHolder(
        holder: EntretienViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        if(payloads.isNotEmpty() && payloads[0] == 0){
            holder.collapseExpandedView()
        }else{
            super.onBindViewHolder(holder, position, payloads)

        }
    }

    override fun getItemCount(): Int {
        return entretien.size
    }
}