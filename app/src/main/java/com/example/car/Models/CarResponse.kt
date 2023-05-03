package com.example.car.Models

data class CarResponse (
    val success: Boolean,
    val error: String,
    val message: String,
    val cars: List<Car>
        )