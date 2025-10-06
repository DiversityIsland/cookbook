package ru.cookbook.util

import ru.cookbook.dto.image.ImageUploadRequest
import ru.cookbook.entity.Image
import java.time.OffsetDateTime
import java.util.UUID

fun imageUploadRequest(
    data: ByteArray = "Test Image Data".toByteArray(),
    contentType: String = "image/jpeg"
) = ImageUploadRequest(
    data = data,
    contentType = contentType
)

fun image(
    id: UUID = UUID.randomUUID(),
    data: ByteArray = "Test Image Data".toByteArray(),
    contentType: String = "image/jpeg",
    size: Long = data.size.toLong(),
    createdAt: OffsetDateTime = OffsetDateTime.now()
) = Image(
    id = id,
    data = data,
    contentType = contentType,
    size = size,
    createdAt = createdAt
)
