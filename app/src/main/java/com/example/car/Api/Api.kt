package com.example.car.Api

import com.example.car.Models.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("user/signup")
    fun Register(
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("email") email:String,
        @Field("role") role:String,
    ):Call<UserResponse>

    @FormUrlEncoded
    @POST("user/signin")
    fun Login(
        @Field("username") username:String,
        @Field("password") password:String,
    ):Call<UserResponse>
}