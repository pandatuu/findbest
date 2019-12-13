package com.example.findbest.register.model


data class RegistModel(
    val phone: String,
    val country: String,
    var vCode: String,
    var pwd: String
)