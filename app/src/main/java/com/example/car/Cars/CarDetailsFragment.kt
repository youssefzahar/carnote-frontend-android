package com.example.car.Cars

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Models.Car
import com.example.car.Models.CarResponse
import com.example.car.R
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CarDetailsFragment : Fragment() {

    private lateinit var car: Car
    private lateinit var modelCar: TextView
    private lateinit var marqueCar: TextView
    private lateinit var CarImage: ImageView
    private lateinit var cerOwner: TextView
    private lateinit var descriptionCar: TextView
    private lateinit var carburantCar: TextView
    private lateinit var puissanceCar: TextView
    private lateinit var circulationdateCar: TextView
    private lateinit var idCar: TextView

    private lateinit var updateBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        car = arguments?.getParcelable<Car>("car")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_car_details, container, false)

        updateBtn = view.findViewById(R.id.updateCarBtn)
        deleteBtn = view.findViewById(R.id.deleteCarBtn)

        updateBtn.setOnClickListener{
            val fragment = ModifyCarFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }


        deleteBtn.setOnClickListener {
            RetrofitClient.carinstace.DeleteCar(car._id)
                .enqueue(object: Callback<CarResponse> {
                    override fun onResponse(call: Call<CarResponse>, response: Response<CarResponse>) {
                        if(response.code() == 200)
                        {
                            findNavController().navigate(R.id.carsforsale_recycler_view)
                            /*SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Welcome To CarNote")
                                .show();*/
                        } else if (response.code() == 500)
                            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Error !")
                                .setConfirmText("OK!")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()
                    }

                    override fun onFailure(call: Call<CarResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message , Toast.LENGTH_LONG).show()
                    }
                })
        }


        return view
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the TextViews
        CarImage = view.findViewById(R.id.carImage)

        marqueCar = view.findViewById(R.id.marquecar)
        modelCar = view.findViewById(R.id.modelcar)
        descriptionCar = view.findViewById(R.id.cardescription)
        carburantCar = view.findViewById(R.id.carburant)
        circulationdateCar = view.findViewById(R.id.curculatiodate)
        idCar = view.findViewById(R.id.idcar)
        puissanceCar = view.findViewById(R.id.puissance)
        cerOwner = view.findViewById(R.id.carowner)

        idCar.visibility = View.GONE
        cerOwner.visibility = View.GONE

        // Set the text of the TextViews with the car data
        Picasso.get().load(RetrofitClient.URL + "img/" + car.image).into(CarImage)


        marqueCar.text = car.marque
        modelCar.text = car.modele
        descriptionCar.text = car.description
        cerOwner.text = car.owned_by
        circulationdateCar.text = car.date_circulation
        carburantCar.text = car.carburant
        puissanceCar.text = car.puissance
        idCar.text = car._id
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefsCar", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("carId", idCar.text.toString())
        editor.apply()

    }

}