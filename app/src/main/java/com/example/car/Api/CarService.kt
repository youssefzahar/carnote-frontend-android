package com.example.car.Api

import com.example.car.Models.CarResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CarService {



    @Multipart
    @POST("car/addcar")
    fun AddCar(
        @Header("Authorization") token: String,
        @Part("modele") modele:String,
        @Part("marque") marque:String,
        @Part("puissance") puissance:Int,
        @Part("carburant") carburant:String,
        @Part("description") description:String,
        @Part("date_circulation") date_circulation:String,
        @Part image : MultipartBody.Part
    ): Call<CarResponse>

    @GET("car/userCars")
    fun UserCars(@Header("Authorization") token: String):Call<CarResponse>

    /*  @GET("car/carsForSale")
      fun CarsForSale():Call<MutableList<Car>>*/

    @GET("car/carsForSale")
    fun getAllCars(): Call<CarResponse>


    @FormUrlEncoded
    @POST("car/deleteCar")
    fun DeleteCar(
        @Header("Authorization") token: String,
        @Field("_id") _id:String,
    ): Call<CarResponse>


    @FormUrlEncoded
    @PUT("car/updatecar")
    fun updateCar(
        @Header("Authorization") token: String,
        @Field("_id") _id:String,
        @Field("description") description:String,
        @Field("date_circulation") date_circulation:String,
    ): Call<CarResponse>
}