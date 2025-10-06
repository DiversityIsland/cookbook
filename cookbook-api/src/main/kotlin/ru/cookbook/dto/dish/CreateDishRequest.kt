package ru.cookbook.dto.dish

import java.util.UUID

data class CreateDishRequest(
    val name: String,
    val description: String,
    val imageId: UUID? = null
)
