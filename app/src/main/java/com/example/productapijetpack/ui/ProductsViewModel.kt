package com.example.productapijetpack.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.productapijetpack.ProductsApplication
import com.example.productapijetpack.data.ProductRepository
import com.example.productapijetpack.model.Product
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface ProductUiState{
    data class Success(val products:List<Product>) :ProductUiState
    object Error:ProductUiState
    object Loading:ProductUiState
}

class ProductsViewModel(private val productRepository: ProductRepository):ViewModel() {
    var productUiState:ProductUiState by mutableStateOf(ProductUiState.Loading)
        private set

    init{
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            productUiState=ProductUiState.Loading
            productUiState= try {
                ProductUiState.Success(productRepository.getProducts())
            }
            catch (e:IOException){
                ProductUiState.Error
            }
            catch (e:HttpException){
                ProductUiState.Error
            }
        }
    }
    companion object{
        val Factory:ViewModelProvider.Factory= viewModelFactory {
            initializer {
                val application=(this[APPLICATION_KEY] as ProductsApplication)
                val productsRepository= application.container.productsRepository
                ProductsViewModel(productRepository = productsRepository)
            }
        }
    }
}