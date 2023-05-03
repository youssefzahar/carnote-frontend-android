package com.example.car.Api

import com.example.car.Models.CarResponse
import com.example.car.Models.ProductResponse
import retrofit2.Call
import retrofit2.http.*

interface ProductService {

    @FormUrlEncoded
    @POST("product/addproduct")
    fun AddProduct(
        @Header("Authorization") token: String,
        @Field("title") title:String,
        @Field("stock") stock:String,
        @Field("prix") prix: String,
      //  @Field("owned_by") owned_by:String,
 //       @Field("image") image:String,

        ): Call<ProductResponse>

    @GET("product/userProducts")
    fun UserProducts(@Header("Authorization") token: String): Call<ProductResponse>



    @GET("car/productsForSale")
    fun getAllProducts(): Call<ProductResponse>

}