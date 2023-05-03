package com.example.car.Api

import com.example.car.Models.Car
import com.example.car.Models.CarResponse
import com.example.car.Models.UserResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.Date

interface CarService {


    @FormUrlEncoded
    @POST("car/addcar")
    fun AddCar(
        @Header("Authorization") token: String,
        @Field("modele") modele:String,
        @Field("marque") marque:String,
        @Field("puissance") puissance:Int,
        @Field("carburant") carburant:String,
        @Field("description") description:String,
        @Field("date_circulation") date_circulation:String,
        ): Call<CarResponse>

    @GET("car/userCars")
    fun UserCars(@Header("Authorization") token: String):Call<CarResponse>

  /*  @GET("car/carsForSale")
    fun CarsForSale():Call<MutableList<Car>>*/

    @GET("car/carsForSale")
    fun getAllCars(): Call<CarResponse>


}