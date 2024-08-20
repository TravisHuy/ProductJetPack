package com.example.productapijetpack.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productapijetpack.model.Product
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.ActivityNavigatorExtras
import coil.compose.AsyncImage


@Composable
fun ProductApp(viewModel: ProductViewModel){
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "productList")
    {
        composable("productList"){
            ProductListScreen(
                viewModel=viewModel,
                onProductClick= {product ->
                    viewModel.selectProduct(product)
                    navController.navigate("productDetail")
                },
                onAddClick={navController.navigate("addProduct")}
            )
        }
        composable("productDetail"){
            val selectProduct by viewModel.selectProduct.collectAsState()
            selectProduct?.let {product ->
                ProductDetailScreen(
                    product=product,
                    onEditClick={navController.navigate("editProduct")},
                    onDeleteClick={
                        viewModel.deleteProduct(product.id)
                        navController.popBackStack()
                        true
                    },
                    onBackClick={navController.popBackStack()
                                 true}
                )
            }
            if(selectProduct!=null){

            }
        }
        composable("addProduct"){
            AddEditProductScreen(
                onSave={ product ->
                    viewModel.addProduct(product)
                    navController.popBackStack()
                },
                onCancel={navController.popBackStack()}
            )
        }
        composable("editProduct"){
            val selectProduct by viewModel.selectProduct.collectAsState()
            selectProduct?.let { product ->
                AddEditProductScreen(
                    product=product,
                    onSave ={
                            product->
                        viewModel.updateProduct(product)
                        navController.popBackStack()
                        true
                    },
                    onCancel={
                        navController.popBackStack()
                        true
                    })
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(viewModel: ProductViewModel, onProductClick: (Product) ->Unit, onAddClick: () -> Unit) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    Scaffold(topBar = {
        TopAppBar(title = {Text(text = "Products")},
            actions = { IconButton(onClick = { onAddClick}) {
                Icon(Icons.Default.Add, contentDescription ="Add Product" )
            }}
        )
    }) {padding ->
        Box(modifier = Modifier.padding(padding)){
            if(isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            else if(error!=null){
                Text(text = error!!,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center))
            }
            else{
                LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(8.dp)){
                    items(products){
                        product->
                        ProductCard(product,onProductClick)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onProductClick: (Product) -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .clickable { onProductClick(product) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column {
            AsyncImage(
                model = "https://deloyspring.onrender.com/images/${product.imageFileName}",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            contentScale = ContentScale.Crop)
            Text(text = product.name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            Text(text = "Price:$${product.price}", modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(product: Product, onEditClick: () -> Unit, onDeleteClick: () -> Boolean, onBackClick: () -> Boolean) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Product Details")},
            navigationIcon = { IconButton(onClick = {onBackClick}) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }})
        }
    ) {
        padding->
        Column(modifier = Modifier
            .padding(padding)
            .verticalScroll(rememberScrollState())) {
            AsyncImage(
                model = "https://deloyspring.onrender.com/images/${product.imageFileName}",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop)
            Text(text = product.name, style= MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
            Text(text = "Brand:$${product.brand}", modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            Text(text = "Category:$${product.category}", modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            Text(text = "Price:$${product.price}", modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            Text(text = "Description:$${product.description}", modifier = Modifier.padding(16.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { onEditClick }) {
                    Text(text = "Edit")
                }
                Button(onClick = {onDeleteClick}, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Delete")
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(product: Product?=null,onSave: (Product) ->Unit, onCancel: () -> Boolean) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var brand by remember { mutableStateOf(product?.brand ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val categories= listOf("Điện thoại","Laptop","Máy tinh bảng","Phụ kiện","khác")
    var expanded by remember { mutableStateOf(false) }

    val launcher= rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        uri: Uri? ->
        uri?.let {
            imageUri = uri
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = if(product==null) "Add Product" else "Edit Product")},
            navigationIcon ={
                IconButton(onClick = { onCancel}) {
                    Icon(Icons.Default.Close, contentDescription = "Cancel")
                }
            }
        )
    }) {
        padding->
        Column(modifier= Modifier
            .padding(padding)
            .verticalScroll(rememberScrollState())) {

            TextField(value =name , onValueChange ={name=it},
                label = { Text(text = "Name")},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth())

            TextField(value =brand , onValueChange ={brand=it},
                label = { Text(text = "Brand")},
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth())

            // Dropdown Menu for Categories
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { selectedCategory ->
                        DropdownMenuItem(
                            onClick = {
                                category = selectedCategory
                                expanded = false
                            },
                            text = { Text(text = selectedCategory) }
                        )
                    }
                }
            }
            TextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Button(onClick = { launcher.launch("images/*")},modifier=Modifier.padding(16.dp)) {
                Text(text = "Select Image")
            }
            imageUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Button(
                onClick = {
                    val newProduct = Product(
                        id = product?.id ?: 0,
                        name = name,
                        brand = brand,
                        category = category,
                        price = price.toDoubleOrNull() ?: 0.0,
                        description = description,
                        imageFileName = imageUri?.lastPathSegment ?: product?.imageFileName?:""
                    )
                    onSave(newProduct)
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            ) {
                Text("Save")
            }
        }
    }
}
