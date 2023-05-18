package com.example.car.Api

import com.example.car.Models.CarResponse
import com.example.car.UploadImage.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CarService {



    @Multipart
    @POST("car/addcar")
    fun AddCar(
      //  @Header("Authorization") token: RequestBody,
        @Part("modele") modele:RequestBody,
        @Part("marque") marque:RequestBody,
        @Part("puissance") puissance:RequestBody,
        @Part("carburant") carburant:RequestBody,
        @Part("description") description:RequestBody,
        @Part("date_circulation") date_circulation:RequestBody,
        @Part image: MultipartBody.Part,
        ): Call<UploadResponse>

    @GET("car/userCars")
    fun UserCars(@Header("Authorization") token: String):Call<CarResponse>

    /*  @GET("car/carsForSale")
      fun CarsForSale():Call<MutableList<Car>>*/

    @GET("car/carsForSale")
    fun getAllCars(): Call<CarResponse>


    @FormUrlEncoded
    @POST("car/deleteCar")
    fun DeleteCar(
        @Field("_id") _id:String,
    ): Call<CarResponse>


    @FormUrlEncoded
    @PUT("car/updatecar")
    fun updateCar(
        @Field("_id") _id:String,
        @Field("description") description:String,
        @Field("date_circulation") date_circulation:String,
    ): Call<CarResponse>
}