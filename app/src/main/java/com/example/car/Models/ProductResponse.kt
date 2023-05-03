package com.example.car.Models

data class ProductResponse (
    val success: Boolean,
    val error: String,
    val message: String,
    val products: List<Product>
)