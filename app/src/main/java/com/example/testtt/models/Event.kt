package com.example.testtt.models

import java.io.Serializable

data class Event(
    val __v: Int,
    val _id: String,
    val titre: String,
    val datedebut:String,
    val datefin:String,
    val image: String,
    val lieu: String,
    val description: String,
    val nbparticipant: Int,
    val nbPlace: Int


) :Serializable