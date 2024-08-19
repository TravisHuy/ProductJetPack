package com.example.productapijetpack.model

import kotlinx.serialization.SerialName

data class Product(
    val id:Long,
    @SerialName(value = "name")
    var name:String,
    @SerialName(value = "brand")
    var brand:String,
    @SerialName(value = "category")
    var category:String,
    @SerialName(value = "price")
    var price:Double,
    @SerialName(value = "description")
    var description:String,
    @SerialName(value = "imageFileName")
    var imageFileName:String
)