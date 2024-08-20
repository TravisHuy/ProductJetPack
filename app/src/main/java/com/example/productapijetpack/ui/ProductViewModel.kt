package com.example.productapijetpack.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.productapijetpack.data.ProductRepository
import com.example.productapijetpack.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository):ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> =_products

    private val _selectProduct= MutableStateFlow<Product?>(null)
    val selectProduct:StateFlow<Product?> = _selectProduct

    private val _isLoading = MutableStateFlow(false)
    val isLoading :StateFlow<Boolean> =_isLoading

    private val _error= MutableStateFlow<String?>(null)
    val error :StateFlow<String?> = _error

    init {
        fetchProducts()
    }

    fun fetchProducts(){
        viewModelScope.launch {
            _isLoading.value=true
            _error.value=null
            try {
                _products.value=productRepository.getProducts()
            }
            catch (e:Exception){
                _error.value="Failed to fetch products:${e.message}"
            }
            _isLoading.value=false
        }
    }

    fun selectProduct(product: Product){
        _selectProduct.value=product
    }

    fun addProduct(product: Product){
        viewModelScope.launch {
            _isLoading.value=true
            _error.value=null
            try {
                productRepository.createProduct(product)
                fetchProducts()
            }
            catch (e:Exception){
                _error.value="Failed to add product: ${e.message}"
            }
            _isLoading.value=false
        }
    }

   fun updateProduct(product: Product){
       viewModelScope.launch {
           _isLoading.value=true
           _error.value=null
           try {
               productRepository.updateProduct(product.id,product)
               fetchProducts()
           }
           catch (e:Exception){
               _error.value="Failed to update product: ${e.message}"
           }
           _isLoading.value=false
       }
   }

   fun deleteProduct(id:Long){
       viewModelScope.launch {
           _isLoading.value=true
           _error.value=null
           try {
               productRepository.deleteProduct(id)
               fetchProducts()
           }
           catch (e:Exception){
               _error.value="Failed to delete product: ${e.message}"
           }
           _isLoading.value=false
       }
   }
}
//This class is a ViewModelProvider.Factory implementation that creates instances of ProductViewModel
class ProductViewModelFactory(private val productRepository: ProductRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProductViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}