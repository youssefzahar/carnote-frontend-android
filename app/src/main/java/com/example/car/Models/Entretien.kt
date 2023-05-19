package com.example.car.Models

import java.util.Date

data class Entretien (

    val title : String,
    val description : String,
    val date : String,
    val entretien : List<Entretien>,
    var isExpandable: Boolean = false

)