package com.example.car.Api

import com.example.car.Models.CarResponse
import com.example.car.Models.ProductResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ProductService {

    @GET("product/getAllProducts")
    fun getAllProducts(): Call<ProductResponse>
/*
    @GET("product/getAllProducts")
    fun getAllProducts(): Call<ProductResponse>*/

    @Multipart
    @POST("product/addProduct")
    fun AddCar(
        @Header("Authorization") token: String,
        @Part("title") title:String,
        @Part("stock") stock:Int,
        @Part("prix") prix:Int,
        @Part("description") description:String,
        @Part image : MultipartBody.Part
    ): Call<ProductResponse>


    @GET("product/usersProducts")
    fun UsersProducts(@Header("Authorization") token: String):Call<ProductResponse>

    @FormUrlEncoded
    @POST("product/deleteProduct")
    fun DeleteProduct(
        @Header("Authorization") token: String,
        @Field("_id") _id:String,
    ): Call<ProductResponse>


    @FormUrlEncoded
    @PUT("product/updateProduct")
    fun updateProduct(
        @Header("Authorization") token: String,
        @Field("_id") _id:String,
        @Field("stock") stock:Int,
        @Field("prix") prix:Int,
        @Field("description") description: String,
    ): Call<ProductResponse>

}