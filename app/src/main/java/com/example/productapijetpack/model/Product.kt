package com.example.productapijetpack.model

import kotlinx.serialization.SerialName

data class Product(
    val id:Long,
    var name:String,
    var brand:String,
    var category:String,
    var price:Double,
    var description:String,
    var imageFileName:String
)