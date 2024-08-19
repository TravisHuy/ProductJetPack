package com.example.productapijetpack.network

import com.example.productapijetpack.model.Product
import retrofit2.http.GET

interface ProductApiService {
    @GET("api/photos")
    suspend fun getProducts():List<Product>
}