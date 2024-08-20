package com.example.productapijetpack.data

import com.example.productapijetpack.model.Product
import com.example.productapijetpack.network.ProductApiService

class ProductRepository(private val productApiService: ProductApiService) {
    suspend fun getProducts() : List<Product> = productApiService.getProducts()
    suspend fun createProduct(product: Product) = productApiService.createProduct(product)
    suspend fun updateProduct(id:Long,product: Product)= productApiService.updateProduct(id,product)
    suspend fun deleteProduct(id:Long)=productApiService.deleteProduct(id)
}