package ru.cookbook.util

import ru.cookbook.dto.recipe.CreateRecipeRequest
import ru.cookbook.dto.recipe.UpdateRecipeRequest
import ru.cookbook.entity.Recipe
import java.time.Duration
import java.time.OffsetDateTime
import java.util.UUID
import ru.cookbook.entity.Dish

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
