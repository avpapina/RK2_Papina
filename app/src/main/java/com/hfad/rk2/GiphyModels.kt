package com.hfad.rk2

data class GiphyResponse(val data: List<GifData>)

data class GifData(
    val id: String,
    val images: ImageUrls
)

data class ImageUrls(
    val fixed_width: ImageInfo
)

data class ImageInfo(
    val url: String,
    val width: Int,
    val height: Int
)