package ru.cookbook.util

import ru.cookbook.dto.dish.CreateDishRequest
import ru.cookbook.dto.dish.DishCardDto
import ru.cookbook.entity.Dish
import ru.cookbook.entity.Image
import java.time.OffsetDateTime
import java.util.UUID

fun createDishRequest(
    name: String = "Test Dish",
    description: String = "Test Description",
    imageId: UUID? = null
) = CreateDishRequest(
    name = name,
    description = description,
    imageId = imageId
)

fun dish(
    id: UUID = UUID.randomUUID(),
    name: String = "Test Dish",
    description: String = "Test Description",
    image: Image? = null,
    createdAt: OffsetDateTime = OffsetDateTime.now(),
    updatedAt: OffsetDateTime = OffsetDateTime.now()
) = Dish(
    id = id,
    name = name,
    description = description,
    image = image,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun dishCardDto(
    id: UUID = UUID.randomUUID(),
    name: String = "Test Dish",
    description: String = "Test Description",
    imageId: UUID? = null,
    recipesCount: Int = 0,
    createdAt: OffsetDateTime = OffsetDateTime.now(),
    updatedAt: OffsetDateTime = OffsetDateTime.now()
) = DishCardDto(
    id = id,
    name = name,
    description = description,
    imageId = imageId,
    recipesCount = recipesCount,
    createdAt = createdAt,
    updatedAt = updatedAt
)
