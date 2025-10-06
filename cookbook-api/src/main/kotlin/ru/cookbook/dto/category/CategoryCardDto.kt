package ru.cookbook.dto.category

import java.time.OffsetDateTime
import java.util.UUID

data class CategoryCardDto(
    val id: UUID,
    val name: String,
    val description: String?,
    val dishesCount: Int,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
