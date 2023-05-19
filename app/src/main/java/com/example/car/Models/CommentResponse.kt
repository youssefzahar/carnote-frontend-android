package com.example.car.Models

data class CommentResponse (
    val success: Boolean,
    val error: String,
    val message: String,
    val comments: List<Comment>

)