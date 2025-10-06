package ru.cookbook.dto.category

data class UpdateCategoryRequest(
    val name: String,
    val description: String?
)
