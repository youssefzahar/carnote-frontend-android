package com.example.car.Models


data class UserResponse (
    val token: String,
    val success: Boolean,
    val error: String,
    val status: Int,
    val user: User
    )