package com.example.car.Api

import com.example.car.Models.CommentResponse
import com.example.car.Models.ProductResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentService {


    @FormUrlEncoded
    @POST("comment/add")
    fun AddComment(
        @Header("Authorization") token: String,
        @Field("idProduct") idProduct: String,
        @Field("description") description:String,
    ): Call<CommentResponse>

    @GET("comment/getAll/{idProduct}")
    fun getUsersComments(@Path("idProduct") idProduct: String): Call<CommentResponse>

    @FormUrlEncoded
    @POST("comment/deleteAllComment")
    fun DeleteProduct(
        @Header("Authorization") token: String,
        @Field("_id") _id:String,
    ): Call<ProductResponse>

}