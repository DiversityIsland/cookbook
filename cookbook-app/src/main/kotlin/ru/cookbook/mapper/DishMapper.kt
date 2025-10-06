package ru.cookbook.mapper

import java.time.OffsetDateTime
import ru.cookbook.dto.dish.CreateDishRequest
import ru.cookbook.dto.dish.DishCardDto
import ru.cookbook.dto.dish.DishGridDto
import ru.cookbook.dto.dish.UpdateDishRequest
import ru.cookbook.entity.Dish

fun CreateDishRequest.toEntity() = Dish(
    name = name,
    description = description,
    image = null
)

fun Dish.toCardDto() = DishCardDto(
    id = id,
    name = name,
    description = description,
    imageId = image?.id,
    recipesCount = recipes.size,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Dish.toGridDto() = DishGridDto(
    id = id,
    name = name,
    imageId = image?.id
)

fun Dish.update(request: UpdateDishRequest) {
    name = request.name
    description = request.description
    updatedAt = OffsetDateTime.now()
}
