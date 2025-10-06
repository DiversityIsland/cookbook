package ru.cookbook.dto.dish

import java.util.UUID

data class UpdateDishRequest(
    val name: String,
    val description: String,
    val imageId: UUID? = null
)
