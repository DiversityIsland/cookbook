package ru.cookbook.mapper

import ru.cookbook.dto.recipe.CookingStepDto
import ru.cookbook.dto.recipe.CreateCookingStepRequest
import ru.cookbook.dto.recipe.CreateRecipeRequest
import ru.cookbook.dto.recipe.IngredientDto
import ru.cookbook.dto.recipe.RecipeCardDto
import ru.cookbook.dto.recipe.RecipeFullDto
import ru.cookbook.dto.recipe.RecipeGridDto
import ru.cookbook.dto.recipe.UpdateRecipeRequest
import ru.cookbook.entity.CookingStep
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

fun Recipe.toFullDto() = RecipeFullDto(
    id = id,
    dishId = dish.id,
    name = name,
    description = description,
    cookingTime = cookingTime,
    servings = servings,
    ingredients = ingredients.map { it.toDto() },
    steps = steps.map { it.toDto() },
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ru.cookbook.entity.Ingredient.toDto() = IngredientDto(
    id = id,
    name = name,
    amount = amount,
    unit = unit.name,
    isOptional = isOptional
)

fun CookingStep.toDto() = CookingStepDto(
    id = id,
    stepNumber = stepNumber,
    description = description,
    estimatedTime = estimatedTime
)

fun CreateCookingStepRequest.toEntity(recipe: Recipe) = CookingStep(
    recipe = recipe,
    stepNumber = stepNumber,
    description = description,
    estimatedTime = estimatedTime
)
