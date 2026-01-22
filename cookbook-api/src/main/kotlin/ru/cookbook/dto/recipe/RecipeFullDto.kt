package ru.cookbook.dto.recipe

import java.time.Duration
import java.time.OffsetDateTime
import java.util.UUID

data class RecipeFullDto(
    val id: UUID,
    val dishId: UUID,
    val name: String,
    val description: String,
    val cookingTime: Duration,
    val servings: Int,
    val ingredients: List<IngredientDto>,
    val steps: List<CookingStepDto>,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

data class IngredientDto(
    val id: UUID,
    val name: String,
    val amount: java.math.BigDecimal,
    val unit: String,
    val isOptional: Boolean
)

data class CookingStepDto(
    val id: UUID,
    val stepNumber: Int,
    val description: String,
    val estimatedTime: Duration?
)
