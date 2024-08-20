package com.example.productapijetpack.network

import com.example.productapijetpack.model.Product
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApiService {
    @GET("api/products")
    suspend fun getProducts():List<Product>
    @POST("api/products")
    suspend fun createProduct(@Body product: Product):Product
    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id:Long,@Body product: Product)
    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id")id:Long)
}