package com.example.car.Api

import com.example.car.Models.EntretienResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface EntretienService {

    @FormUrlEncoded
    @POST("entretien/addEntretien")
    fun AddEntretien(
        @Header("Authorization") token: String,
        @Field("title") title:String,
        @Field("description") description:String,
        @Field("date") date:String,

        ): Call<EntretienResponse>

    @GET("entretien/userEntretiens")
    fun UserEntretien(@Header("Authorization") token: String): Call<EntretienResponse>

}