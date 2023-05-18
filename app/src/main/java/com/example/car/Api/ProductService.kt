package com.example.car.Api

import com.example.car.Models.CarResponse
import com.example.car.Models.ProductResponse
import retrofit2.Call
import retrofit2.http.*

interface ProductService {

    @GET("product/getAllProducts")
    fun getAllProducts(): Call<ProductResponse>
/*
    @GET("product/getAllProducts")
    fun getAllProducts(): Call<ProductResponse>*/

    @FormUrlEncoded
    @POST("product/addProduct")
    fun AddCar(
        @Header("Authorization") token: String,
        @Field("title") title:String,
        @Field("stock") stock:Int,
        @Field("prix") prix:Int,
        @Field("description") description:String,
    ): Call<ProductResponse>

    @GET("product/usersProducts")
    fun UsersProducts(@Header("Authorization") token: String):Call<ProductResponse>

    @FormUrlEncoded
    @POST("product/deleteProduct")
    fun DeleteProduct(
        @Field("_id") _id:String,
    ): Call<ProductResponse>


    @FormUrlEncoded
    @PUT("product/updateProduct")
    fun updateProduct(
        @Field("_id") _id:String,
        @Field("stock") stock:Int,
        @Field("prix") prix:Int,
        @Field("description") description: String,
    ): Call<ProductResponse>
}