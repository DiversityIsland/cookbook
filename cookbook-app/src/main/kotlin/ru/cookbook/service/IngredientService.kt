package ru.cookbook.service

import org.springframework.stereotype.Service
import ru.cookbook.dto.recipe.CreateIngredientRequest
import ru.cookbook.entity.Ingredient
import ru.cookbook.enums.MeasurementUnit
import ru.cookbook.repository.IngredientRepository
import ru.cookbook.repository.RecipeRepository
import ru.cookbook.util.findByIdOrThrow
import java.util.UUID

@Service
class IngredientService(
    private val ingredientRepository: IngredientRepository,
    private val recipeRepository: RecipeRepository
) {
    fun addIngredient(recipeId: UUID, request: CreateIngredientRequest): UUID {
        val recipe = recipeRepository.findByIdOrThrow(recipeId)
        
        val ingredient = Ingredient(
            recipe = recipe,
            name = request.name,
            amount = request.amount,
            unit = MeasurementUnit.valueOf(request.unit),
            isOptional = request.isOptional
        )
        
        return ingredientRepository.save(ingredient).id
    }

    fun deleteIngredient(id: UUID) {
        ingredientRepository.deleteById(id)
    }
}
