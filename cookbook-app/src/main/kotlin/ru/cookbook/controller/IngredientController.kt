package ru.cookbook.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.cookbook.dto.recipe.CreateIngredientRequest
import ru.cookbook.service.IngredientService
import java.util.UUID

@RestController
@RequestMapping("/api/v1")
class IngredientController(
    private val ingredientService: IngredientService
) {
    @PostMapping("/recipes/{recipeId}/ingredients")
    fun addIngredient(
        @PathVariable recipeId: UUID,
        @RequestBody request: CreateIngredientRequest
    ): UUID = ingredientService.addIngredient(recipeId, request)

    @DeleteMapping("/ingredients/{id}")
    fun deleteIngredient(@PathVariable id: UUID) =
        ingredientService.deleteIngredient(id)
}
