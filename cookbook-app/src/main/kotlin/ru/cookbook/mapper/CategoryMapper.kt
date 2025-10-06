package ru.cookbook.mapper

import ru.cookbook.dto.category.CategoryCardDto
import ru.cookbook.dto.category.CategoryGridDto
import ru.cookbook.dto.category.CreateCategoryRequest
import ru.cookbook.dto.category.UpdateCategoryRequest
import ru.cookbook.entity.Category
import java.time.OffsetDateTime

fun CreateCategoryRequest.toEntity() = Category(
    name = name,
    description = description
)

fun Category.toCardDto() = CategoryCardDto(
    id = id,
    name = name,
    description = description,
    dishesCount = dishLinks.size,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Category.toGridDto() = CategoryGridDto(
    id = id,
    name = name,
    dishesCount = dishLinks.size
)

fun Category.update(request: UpdateCategoryRequest) {
    name = request.name
    description = request.description
    updatedAt = OffsetDateTime.now()
}
