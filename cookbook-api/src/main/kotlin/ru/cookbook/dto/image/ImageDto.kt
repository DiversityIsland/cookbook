package ru.cookbook.dto.image

import java.time.OffsetDateTime
import java.util.UUID

data class ImageDto(
    val id: UUID,
    val contentType: String,
    val size: Long,
    val createdAt: OffsetDateTime
)
