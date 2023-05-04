package com.example.car.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize

data class Product (
    val _id: String,
    val title: String,
    val stock: Int,
    val prix: Int,
    val description: String,
    val owned_by: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val image: String,
        ): Serializable, Parcelable