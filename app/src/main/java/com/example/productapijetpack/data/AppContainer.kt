package com.example.productapijetpack.data

import com.example.productapijetpack.network.ProductApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val productsRepository:ProductRepository
}

class DefaultAppContainer: AppContainer{

    private val baseUrl="https://deloyspring.onrender.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java)
    }

    override val productsRepository: ProductRepository by lazy {
        NetworkProductRepository(retrofitService)
    }

}