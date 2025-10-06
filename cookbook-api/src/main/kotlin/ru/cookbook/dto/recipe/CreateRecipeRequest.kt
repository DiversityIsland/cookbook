package ru.cookbook.dto.recipe

import java.time.Duration

data class CreateRecipeRequest(
    val name: String,
    val description: String,
    val cookingTime: Duration,
    val servings: Int
)
