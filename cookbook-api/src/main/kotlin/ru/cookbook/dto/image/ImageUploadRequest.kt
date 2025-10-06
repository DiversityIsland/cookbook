package ru.cookbook.dto.image

data class ImageUploadRequest(
    val data: ByteArray,
    val contentType: String
)
