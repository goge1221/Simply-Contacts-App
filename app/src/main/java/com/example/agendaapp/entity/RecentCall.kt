package com.example.agendaapp.entity

import java.util.*

data class RecentCall(
    val name : String,
    val phoneNumber : String,
    val date : Date,
    val duration : Int,
    val type : Int
    )
