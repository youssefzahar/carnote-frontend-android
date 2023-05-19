package com.example.car.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize

data class Comment (
    val _id: String,
    val description: String,
    val idProduct: String,
    val idUser: String,
    val userimage: String,
    val username: String
    ): Serializable, Parcelable