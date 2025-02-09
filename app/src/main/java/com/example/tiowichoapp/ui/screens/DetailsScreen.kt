package com.example.tiowichoapp.ui.screens

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiowichoapp.data.models.Item
import com.example.tiowichoapp.data.network.Repository
import com.example.tiowichoapp.data.states.ResourceState
import com.example.tiowichoapp.ui.components.LoadingIndicator
import com.example.tiowichoapp.viewmodels.DetailsViewModel
import com.example.tiowichoapp.viewmodels.ViewModelFactory

@Composable
fun DetailsScreen(itemId: String?, repository: Repository) {
    val factory = ViewModelFactory(repository)
    val viewModel: DetailsViewModel = viewModel(factory = factory)

    // Fetch details when the composable is launched
    LaunchedEffect(itemId) {
        itemId?.toIntOrNull()?.let { id ->
            viewModel.fetchItemDetails(id)
        }
    }

    // Observe the state of the ViewModel
    val state by viewModel.itemState.collectAsState()

    // Display content based on the state
    when (state) {
        is ResourceState.Loading -> LoadingIndicator()
        is ResourceState.Success -> {
            val item = (state as ResourceState.Success<Item>).data
            ItemDetails(item)
        }
        is ResourceState.Error -> Text("Error: ${(state as ResourceState.Error).message}")
    }
}

@Composable
fun ItemDetails(item: Item?) {
    item?.let {
        Text(
            text = "Title: ${it.title}\nDescription: ${it.description}",
            style = MaterialTheme.typography.h6
        )
    }
}
