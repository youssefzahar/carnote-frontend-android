package com.example.car.Cars

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.car.Api.RetrofitClient
import com.example.car.Models.Car
import com.example.car.R
import com.squareup.picasso.Picasso


class CarDetailsFragment : Fragment() {

    private lateinit var car: Car
    private lateinit var modelCar: TextView
    private lateinit var marqueCar: TextView
    private lateinit var CarImage: ImageView

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        car = arguments?.getParcelable<Car>("car")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_details, container, false)
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the TextViews
        CarImage = view.findViewById(R.id.carImage)

        marqueCar = view.findViewById(R.id.marquecar)
        modelCar = view.findViewById(R.id.modelcar)

        // Set the text of the TextViews with the car data
        Picasso.get().load(RetrofitClient.URL + "img/" + car.image).into(CarImage)


        marqueCar.text = car.marque
        modelCar.text = car.modele

    }

}