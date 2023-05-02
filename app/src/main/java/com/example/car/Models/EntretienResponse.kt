package com.example.car.Models

data class EntretienResponse (
    val success: Boolean,
    val error: String,
    val status: Int,
    val entretien: List<Entretien>

)