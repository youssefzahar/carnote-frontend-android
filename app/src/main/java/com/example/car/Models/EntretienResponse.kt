package com.example.car.Models


data class EntretienResponse (
    val success: Boolean,
    val error: String,
    val message: String,
    val entretien: List<Entretien>
)