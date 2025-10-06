package ru.cookbook.dto.recipe

import java.time.Duration
import java.util.UUID

data class RecipeGridDto(
    val id: UUID,
    val dishId: UUID,
    val name: String,
    val cookingTime: Duration,
    val servings: Int,
    val ingredientsCount: Int,
    val stepsCount: Int
)
