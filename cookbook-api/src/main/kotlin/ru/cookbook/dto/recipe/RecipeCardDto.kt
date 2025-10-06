package ru.cookbook.dto.recipe

import java.time.Duration
import java.time.OffsetDateTime
import java.util.UUID

data class RecipeCardDto(
    val id: UUID,
    val dishId: UUID,
    val name: String,
    val description: String,
    val cookingTime: Duration,
    val servings: Int,
    val ingredientsCount: Int,
    val stepsCount: Int,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
