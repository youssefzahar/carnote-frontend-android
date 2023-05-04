package com.example.car.Api

import com.example.car.Models.ProductResponse
import retrofit2.Call
import retrofit2.http.GET

interface ProductService {

    @GET("product/getAllProducts")
    fun getAllProducts(): Call<ProductResponse>
/*
    @GET("product/getAllProducts")
    fun getAllProducts(): Call<ProductResponse>*/
}