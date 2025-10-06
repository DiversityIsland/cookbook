package ru.cookbook.mapper

import ru.cookbook.dto.image.ImageDto
import ru.cookbook.dto.image.ImageUploadRequest
import ru.cookbook.entity.Image

fun ImageUploadRequest.toEntity() = Image(
    data = data,
    contentType = contentType,
    size = data.size.toLong()
)

fun Image.toDto() = ImageDto(
    id = id,
    contentType = contentType,
    size = size,
    createdAt = createdAt
)
