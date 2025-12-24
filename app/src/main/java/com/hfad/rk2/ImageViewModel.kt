package com.hfad.rk2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ImageState{
    object Loading: ImageState()
    data class Success(val images: List<GifData>) : ImageState()
    data class Error(val message: String) : ImageState()
}

class ImageViewModel : ViewModel() {
    private val _state = MutableStateFlow<ImageState>(ImageState.Loading)
    val state: StateFlow<ImageState> = _state.asStateFlow()

    private var currentPage = 1

    init {
        loadImages()
    }

    fun loadImages() {
        viewModelScope.launch {
            _state.value = ImageState.Loading

            try {
                val response = ApiClient.instance.getGifs(limit = 10)
                _state.value = ImageState.Success(response.data)
            } catch (e: Exception) {
                _state.value = ImageState.Error("Ошибка: ${e.message}")
            }
        }
    }

}