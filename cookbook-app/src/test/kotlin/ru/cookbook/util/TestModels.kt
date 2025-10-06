package ru.cookbook.util

import ru.cookbook.dto.dish.CreateDishRequest
import ru.cookbook.dto.dish.DishCardDto
import ru.cookbook.dto.recipe.CreateRecipeRequest
import ru.cookbook.dto.recipe.RecipeCardDto
import ru.cookbook.dto.recipe.UpdateRecipeRequest
import ru.cookbook.entity.Dish
import ru.cookbook.entity.Image
import ru.cookbook.entity.Recipe
import java.time.Duration
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

fun createRecipeRequest(
    name: String = "Test Recipe",
    description: String = "Test Recipe Description",
    cookingTime: Duration = Duration.ofMinutes(30),
    servings: Int = 4
) = CreateRecipeRequest(
    name = name,
    description = description,
    cookingTime = cookingTime,
    servings = servings
)

fun updateRecipeRequest(
    name: String = "Updated Recipe",
    description: String = "Updated Recipe Description",
    cookingTime: Duration = Duration.ofMinutes(45),
    servings: Int = 6
) = UpdateRecipeRequest(
    name = name,
    description = description,
    cookingTime = cookingTime,
    servings = servings
)

fun recipe(
    id: UUID = UUID.randomUUID(),
    dish: Dish = dish(),
    name: String = "Test Recipe",
    description: String = "Test Recipe Description",
    cookingTime: Duration = Duration.ofMinutes(30),
    servings: Int = 4,
    createdAt: OffsetDateTime = OffsetDateTime.now(),
    updatedAt: OffsetDateTime = OffsetDateTime.now()
) = Recipe(
    id = id,
    dish = dish,
    name = name,
    description = description,
    cookingTime = cookingTime,
    servings = servings,
    createdAt = createdAt,
    updatedAt = updatedAt
)
