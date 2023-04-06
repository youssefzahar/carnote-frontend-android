package com.example.car.Models

data class User (
    val username: String,
    val email: String,
    val role: String,
    val password: String,
    val isVerified: Boolean,
    val active: Boolean,
    val otp: String,
)