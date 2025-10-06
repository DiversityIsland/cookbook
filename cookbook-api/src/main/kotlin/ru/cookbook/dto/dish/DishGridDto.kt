package ru.cookbook.dto.dish

import java.util.UUID

data class DishGridDto(
    val id: UUID,
    val name: String,
    val imageId: UUID?
)
