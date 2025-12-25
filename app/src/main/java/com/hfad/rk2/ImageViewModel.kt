package com.hfad.rk2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ImageState {
    object Loading : ImageState()
    data class Success(
        val images: List<GifData>,
        val isLoadingNextPage: Boolean = false
    ) : ImageState()
    data class Error(val message: String) : ImageState()
}

class ImageViewModel : ViewModel() {
    private val _state = MutableStateFlow<ImageState>(ImageState.Loading)
    val state: StateFlow<ImageState> = _state.asStateFlow()

    private val cachedImages = mutableListOf<GifData>()
    private var currentOffset = 0
    private val limit = 10

    init {
        loadImages()
    }

    fun loadImages() {
        viewModelScope.launch {
            _state.value = ImageState.Loading
            try {
                val response = ApiClient.instance.getGifs(limit = limit, offset = 0)
                cachedImages.clear()
                cachedImages.addAll(response.data)
                currentOffset = response.data.size
                _state.value = ImageState.Success(cachedImages.toList())
            } catch (e: Exception) {
                _state.value = ImageState.Error("Ошибка: ${e.message}")
            }
        }
    }

    fun loadNextPage() {
        val currentState = _state.value
        if (currentState is ImageState.Success && currentState.isLoadingNextPage) {
            return
        }

        viewModelScope.launch {
            try {
                _state.value = ImageState.Success(
                    images = cachedImages.toList(),
                    isLoadingNextPage = true
                )

                val response = ApiClient.instance.getGifs(limit = limit, offset = currentOffset)
                cachedImages.addAll(response.data)
                currentOffset += response.data.size

                _state.value = ImageState.Success(images = cachedImages.toList())
            } catch (e: Exception) {
                _state.value = ImageState.Success(images = cachedImages.toList())
            }
        }
    }
}