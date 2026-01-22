package ru.cookbook.dto.recipe

import java.time.Duration

data class CreateCookingStepRequest(
    val stepNumber: Int,
    val description: String,
    val estimatedTime: Duration?
)
