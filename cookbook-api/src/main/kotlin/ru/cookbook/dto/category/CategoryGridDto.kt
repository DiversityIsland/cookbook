package ru.cookbook.dto.category

import java.util.UUID

data class CategoryGridDto(
    val id: UUID,
    val name: String,
    val dishesCount: Int
)
