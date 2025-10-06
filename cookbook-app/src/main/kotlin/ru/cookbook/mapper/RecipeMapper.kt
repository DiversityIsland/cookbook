package ru.cookbook.mapper

import ru.cookbook.dto.recipe.CreateRecipeRequest
import ru.cookbook.dto.recipe.RecipeCardDto
import ru.cookbook.dto.recipe.RecipeGridDto
import ru.cookbook.dto.recipe.UpdateRecipeRequest
import ru.cookbook.entity.Dish
import ru.cookbook.entity.Recipe
import java.time.OffsetDateTime

fun CreateRecipeRequest.toEntity(dish: Dish) = Recipe(
    dish = dish,
    name = name,
    description = description,
    cookingTime = cookingTime,
    servings = servings
)

fun Recipe.toCardDto() = RecipeCardDto(
    id = id,
    dishId = dish.id,
    name = name,
    description = description,
    cookingTime = cookingTime,
    servings = servings,
    ingredientsCount = ingredients.size,
    stepsCount = steps.size,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Recipe.toGridDto() = RecipeGridDto(
    id = id,
    dishId = dish.id,
    name = name,
    cookingTime = cookingTime,
    servings = servings,
    ingredientsCount = ingredients.size,
    stepsCount = steps.size
)

fun Recipe.update(request: UpdateRecipeRequest) {
    name = request.name
    description = request.description
    cookingTime = request.cookingTime
    servings = request.servings
    updatedAt = OffsetDateTime.now()
}
