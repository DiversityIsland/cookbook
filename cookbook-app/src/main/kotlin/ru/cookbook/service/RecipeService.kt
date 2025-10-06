package ru.cookbook.service

import org.springframework.stereotype.Service
import ru.cookbook.dto.recipe.CreateRecipeRequest
import ru.cookbook.dto.recipe.RecipeCardDto
import ru.cookbook.dto.recipe.RecipeGridDto
import ru.cookbook.dto.recipe.UpdateRecipeRequest
import ru.cookbook.entity.Dish
import ru.cookbook.entity.Recipe
import ru.cookbook.mapper.toCardDto
import ru.cookbook.mapper.toEntity
import ru.cookbook.mapper.toGridDto
import ru.cookbook.mapper.update
import ru.cookbook.repository.DishRepository
import ru.cookbook.repository.RecipeRepository
import ru.cookbook.util.findByIdOrThrow
import java.util.UUID

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val dishRepository: DishRepository
) {
    fun create(dishId: UUID, request: CreateRecipeRequest): UUID {
        val dish = dishRepository.findByIdOrThrow(dishId)
        val recipe = request.toEntity(dish)
        return recipeRepository.save(recipe).id
    }

    fun getById(id: UUID): RecipeCardDto =
        recipeRepository.findByIdOrThrow(id).toCardDto()

    fun update(id: UUID, request: UpdateRecipeRequest): RecipeCardDto {
        val recipe = recipeRepository.findByIdOrThrow(id)
        recipe.update(request)
        return recipeRepository.save(recipe).toCardDto()
    }

    fun delete(id: UUID) {
        recipeRepository.deleteById(id)
    }

    fun getAll(): List<RecipeGridDto> =
        recipeRepository.findAll().map { it.toGridDto() }

    fun getByDishId(dishId: UUID): List<RecipeGridDto> =
        recipeRepository.findByDishId(dishId).map { it.toGridDto() }
}