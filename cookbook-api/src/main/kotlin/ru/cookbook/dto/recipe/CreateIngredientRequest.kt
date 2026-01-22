package ru.cookbook.dto.recipe

import java.math.BigDecimal

data class CreateIngredientRequest(
    val name: String,
    val amount: BigDecimal,
    val unit: String,
    val isOptional: Boolean = false
)
