package com.example.car.Entretien

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.PopupWindow
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.car.Api.RetrofitClient
import com.example.car.Models.Entretien
import com.example.car.Models.EntretienAdapter
import com.example.car.R
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity

import android.view.Gravity

import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.car.Models.EntretienResponse

import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

import java.util.*


class EntretiensFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var entretien = ArrayList<Entretien>()
    private lateinit var adapter: EntretienAdapter
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_entretiens, container, false)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        addDataToList()
        adapter = EntretienAdapter(listOf()) // create an empty adapter
        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                // Not implemented - we only want to support swipe-to-dismiss
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Get the position of the swiped item
                val position = viewHolder.adapterPosition

                entretien.remove(entretien[position])
                adapter.notifyItemRemoved(position)

                // Remove the item from the adapter
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Inflate the popup layout
            val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_form, null)

            // Find the EditText views in the popup layout
            val titleEditText = popupView.findViewById<EditText>(R.id.titleedittext)
            val descriptionEditText = popupView.findViewById<EditText>(R.id.descedittext)

            // Find the Save button in the popup layout and set its click listener
            val saveButton = popupView.findViewById<Button>(R.id.submitBtn)
            val width = 1000 // Specify the desired width in pixels
            val height = 1000
            val popupWindow = PopupWindow(popupView, width, height, true)
            saveButton.setOnClickListener {
                // Get the selected date, title, and description
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()

                // Log the data to the console
                Log.d("MainActivity", "Selected date: $selectedDate, Title: $title, Description: $description")
                sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("token", null)
                RetrofitClient.entretieninstance.AddEntretien("Bearer $token",title,description, selectedDate)
                    .enqueue(object: Callback<EntretienResponse> {
                        override fun onResponse(call: Call<EntretienResponse>, response: Response<EntretienResponse>) {
                            if(response.code() == 200)
                            {
                                println("successful buahahahaha")
                                /*SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Welcome To CarNote")
                                    .show();  */                      }
                            else if(response.code()==500)
                            //Toast.makeText(requireContext(), "user exists", Toast.LENGTH_LONG).show()

                                println("no no no no hahaha")

                        }

                        override fun onFailure(call: Call<EntretienResponse>, t: Throwable) {
                            Toast.makeText(requireContext(), t.message , Toast.LENGTH_LONG).show()
                        }

                    })

                // Dismiss the popup window
                popupWindow.dismiss()
            }

            // Create a PopupWindow with the inflated layout and display it
            popupWindow.showAtLocation(calendarView, Gravity.CENTER, 0, 300)
        }

        return view
    }

    private fun addDataToList() {
        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        RetrofitClient.entretieninstance.UserEntretien("Bearer $token").enqueue(object : Callback<EntretienResponse> {
            override fun onResponse(call: Call<EntretienResponse>, response: Response<EntretienResponse>) {
                if (response.isSuccessful) {
                    val entretiens = response.body()?.entretien
                    if (entretiens != null) {
                        adapter.entretien = entretiens // update the adapter with the retrieved cars
                        adapter.notifyDataSetChanged() // notify the adapter that the data has changed
                    }
                } else {
                    Log.e(TAG, "Failed to get cars: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<EntretienResponse>, t: Throwable) {
                Log.e(TAG, "Failed to get cars", t)
            }
        })
    }

    private fun getCalendarTimeInMillis(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

}