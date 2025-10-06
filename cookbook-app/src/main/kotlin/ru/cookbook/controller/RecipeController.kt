package ru.cookbook.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.cookbook.dto.recipe.CreateRecipeRequest
import ru.cookbook.dto.recipe.RecipeCardDto
import ru.cookbook.dto.recipe.RecipeGridDto
import ru.cookbook.dto.recipe.UpdateRecipeRequest
import ru.cookbook.service.RecipeService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/recipes")
class RecipeController(
    private val recipeService: RecipeService
) {
    @PostMapping("/dish/{dishId}")
    fun create(
        @PathVariable dishId: UUID,
        @RequestBody request: CreateRecipeRequest
    ): UUID = recipeService.create(dishId, request)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): RecipeCardDto =
        recipeService.getById(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: UpdateRecipeRequest
    ): RecipeCardDto = recipeService.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) =
        recipeService.delete(id)

    @GetMapping
    fun getAll(): List<RecipeGridDto> =
        recipeService.getAll()

    @GetMapping("/dish/{dishId}")
    fun getByDishId(@PathVariable dishId: UUID): List<RecipeGridDto> =
        recipeService.getByDishId(dishId)
}
