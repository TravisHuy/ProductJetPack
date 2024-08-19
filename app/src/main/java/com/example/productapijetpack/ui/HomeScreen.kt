package com.example.productapijetpack.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.productapijetpack.R
import com.example.productapijetpack.model.Product
import com.example.productapijetpack.ui.theme.ProductsTheme

//import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsApp(){
    val scrollBehavior= TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {ProductTopAppBar(scrollBehavior=scrollBehavior)}) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val productsViewModel: ProductsViewModel = viewModel(factory = ProductsViewModel.Factory)
            HomeScreen(productUiState=productsViewModel.productUiState,retryAction=productsViewModel::getProducts
                ,contentPadding=it)
        }
    }
}

@Composable
fun HomeScreen(productUiState: ProductUiState
               , retryAction: () -> Unit
               , modifier: Modifier=Modifier,
               contentPadding:PaddingValues= PaddingValues(0.dp)
) {
    when(productUiState){
        is ProductUiState.Loading -> LoadingScreen(modifier=modifier.fillMaxSize())
        is ProductUiState.Success -> ProductGridScreen(
            productUiState.products,contentPadding=contentPadding,modifier=modifier.fillMaxWidth()
        )
        is ProductUiState.Error -> ErrorScreen(retryAction,modifier=modifier.fillMaxSize())
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier=Modifier) {
    Column(modifier=modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id =R.drawable.baseline_error_24 ), contentDescription = "")
        Text(text = stringResource(id = R.string.loading_failed),modifier=Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(text = stringResource(id = R.string.loading_failed))
        }
    }
}

@Composable
fun ProductGridScreen(products: List<Product>, contentPadding: PaddingValues=PaddingValues(0.dp), modifier: Modifier= Modifier) {
    LazyVerticalGrid(columns = GridCells.Adaptive(150.dp), contentPadding =contentPadding, modifier = modifier.padding(horizontal = 4.dp)){
        items(items = products, key={product->product.id}) {
            product->
            ProductCard(product,modifier= modifier
                .padding(4.dp)
                .fillMaxWidth()
                .aspectRatio(1.5f))
        }
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier=Modifier) {
    Card(modifier=modifier, shape = MaterialTheme.shapes.medium, elevation  = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
        Column() {
            AsyncImage(model = ImageRequest.Builder(context = LocalContext.current).data(product.imageFileName).crossfade(true).build(), contentDescription = stringResource(
                R.string.product_image)
                , error = painterResource(id = R.drawable.baseline_broken_image_24),
                placeholder = painterResource(id = R.drawable.baseline_scatter_plot_24),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop)
            Text(text = product.name,modifier=modifier.padding(10.dp))
            Text(text = product.price.toString(),modifier=modifier.padding(10.dp))
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier=Modifier) {
    Image(painter = painterResource(id = R.drawable.baseline_scatter_plot_24), contentDescription = stringResource(
            R.string.loading),modifier=modifier.size(200.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductTopAppBar(scrollBehavior: TopAppBarScrollBehavior,modifier: Modifier=Modifier) {
    CenterAlignedTopAppBar(scrollBehavior = scrollBehavior, title = {
        Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.headlineSmall)
    }, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    ProductsTheme {
        ErrorScreen({})
    }
}
@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    ProductsTheme {
        LoadingScreen()
    }
}
@Preview(showBackground = true)
@Composable
fun ProductGridsScreenPreview() {
    ProductsTheme {
        val mocDate= List(10){
            Product(0,"","","",0.0,"","")
        }
        ProductGridScreen(products = mocDate)
    }
}
