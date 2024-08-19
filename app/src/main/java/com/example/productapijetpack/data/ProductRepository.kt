package com.example.productapijetpack.data

import com.example.productapijetpack.model.Product
import com.example.productapijetpack.network.ProductApiService

interface ProductRepository {
    suspend fun getProducts() : List<Product>
}

class NetworkProductRepository(
    private val productApiServer:ProductApiService
):ProductRepository{
    override suspend fun getProducts(): List<Product> = productApiServer.getProducts()
}