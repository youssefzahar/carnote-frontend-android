package com.example.car.Api

import com.example.car.Models.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @FormUrlEncoded
    @POST("user/signup")
    fun Register(
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("email") email:String,
        @Field("role") role:String,
        @Field("image") image:String,
    ):Call<UserResponse>

    @FormUrlEncoded
    @POST("user/signin")
    fun Login(
        @Field("username") username:String,
        @Field("password") password:String,
    ):Call<UserResponse>

    @GET("user/profile")
    fun getUser(@Header("Authorization") token: String):Call<UserResponse>

    @FormUrlEncoded
    @POST("user/otpsend")
    fun sendOtp(
    @Field("username") username:String,
    ):Call<UserResponse>

    @FormUrlEncoded
    @POST("user/verifyOTP")
    fun verifyOTP(
        @Field("otp") otp:String,
    ):Call<UserResponse>

    @FormUrlEncoded
    @PUT("user/resetPassword")
    fun resetPassword(
        @Field("username") username:String,
        @Field("password") password:String,
        ):Call<UserResponse>

    @FormUrlEncoded
    @POST("user/desactivateUser")
    fun desactivateUser(@Header("Authorization") token: String):Call<UserResponse>

    @FormUrlEncoded
    @PUT("user/update")
    fun updateuser(
        @Header("Authorization") token: String,
        @Field("email") email:String,
        @Field("password") password:String,
    ):Call<UserResponse>
}