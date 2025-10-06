package ru.cookbook.dto.category

data class CreateCategoryRequest(
    val name: String,
    val description: String?
)
