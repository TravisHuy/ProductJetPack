package com.example.productapijetpack

import android.app.Application
import com.example.productapijetpack.data.AppContainer
import com.example.productapijetpack.data.DefaultAppContainer

class ProductsApplication :Application(){
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container=DefaultAppContainer()
    }
}