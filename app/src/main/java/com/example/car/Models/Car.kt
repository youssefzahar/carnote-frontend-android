package com.example.car.Models

data class Car (
    val _id: String,
    val modele: String,
    val marque: String,
    val puissance: Int,
    val carburant: String,
    val description: String,
    val owned_by: String,
    val visibility: Boolean,
    val date_circulation: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
    )