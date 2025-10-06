package ru.cookbook.util

import ru.cookbook.dto.category.CreateCategoryRequest
import ru.cookbook.dto.category.UpdateCategoryRequest
import ru.cookbook.entity.Category
import java.time.OffsetDateTime
import java.util.UUID

fun createCategoryRequest(
    name: String = "Test Category",
    description: String? = "Test Category Description"
) = CreateCategoryRequest(
    name = name,
    description = description
)

fun updateCategoryRequest(
    name: String = "Updated Category",
    description: String? = "Updated Category Description"
) = UpdateCategoryRequest(
    name = name,
    description = description
)

fun category(
    id: UUID = UUID.randomUUID(),
    name: String = "Test Category",
    description: String? = "Test Category Description",
    createdAt: OffsetDateTime = OffsetDateTime.now(),
    updatedAt: OffsetDateTime = OffsetDateTime.now()
) = Category(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt
)
