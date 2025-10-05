package ru.cookbook.service

import org.springframework.stereotype.Service
import ru.cookbook.repository.RecipeRepository
import ru.cookbook.repository.IngredientRepository
import ru.cookbook.repository.CookingStepRepository
import java.util.UUID

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository,
    private val cookingStepRepository: CookingStepRepository
)
