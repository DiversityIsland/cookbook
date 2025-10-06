package ru.cookbook.dto.dish

import java.time.OffsetDateTime
import java.util.UUID

data class DishCardDto(
    val id: UUID,
    val name: String,
    val description: String,
    val imageId: UUID?,
    val recipesCount: Int,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
