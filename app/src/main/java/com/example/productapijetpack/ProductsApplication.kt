package com.example.productapijetpack

import android.app.Application
import com.example.productapijetpack.data.ProductRepository
import com.example.productapijetpack.network.ProductApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductsApplication :Application(){
    lateinit var productRepository: ProductRepository

    override fun onCreate() {
        super.onCreate()
        val retrofit=Retrofit.Builder()
            .baseUrl("https://deloyspring.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api:ProductApiService by lazy {
            retrofit.create(ProductApiService::class.java)
        }
        productRepository=ProductRepository(api)
    }
}