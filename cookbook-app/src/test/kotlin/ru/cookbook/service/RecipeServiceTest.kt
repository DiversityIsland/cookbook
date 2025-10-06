package ru.cookbook.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import java.util.Optional
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.cookbook.entity.Recipe
import ru.cookbook.repository.DishRepository
import ru.cookbook.repository.RecipeRepository
import ru.cookbook.util.createRecipeRequest
import ru.cookbook.util.dish
import ru.cookbook.util.recipe
import ru.cookbook.util.updateRecipeRequest

@ExtendWith(MockKExtension::class)
class RecipeServiceTest(
    @MockK private val recipeRepository: RecipeRepository,
    @MockK private val dishRepository: DishRepository
) {
    private val recipeService = RecipeService(recipeRepository, dishRepository)

    @Test
    fun `should create recipe`() {
        // given
        val dishId = UUID.randomUUID()
        val dish = dish(id = dishId)
        val request = createRecipeRequest()
        val recipeSlot = slot<Recipe>()
        val savedRecipe = recipe(
            dish = dish,
            name = request.name,
            description = request.description,
            cookingTime = request.cookingTime,
            servings = request.servings
        )
        
        every { dishRepository.findById(dishId) } returns Optional.of(dish)
        every { recipeRepository.save(capture(recipeSlot)) } returns savedRecipe

        // when
        val result = recipeService.create(dishId, request)

        // then
        verify { recipeRepository.save(any()) }
        assertEquals(request.name, recipeSlot.captured.name)
        assertEquals(request.description, recipeSlot.captured.description)
        assertEquals(request.cookingTime, recipeSlot.captured.cookingTime)
        assertEquals(request.servings, recipeSlot.captured.servings)
        assertEquals(dish, recipeSlot.captured.dish)
    }

    @Test
    fun `should get recipe by id`() {
        // given
        val id = UUID.randomUUID()
        val recipe = recipe(id = id)
        
        every { recipeRepository.findById(id) } returns Optional.of(recipe)

        // when
        val result = recipeService.getById(id)

        // then
        assertEquals(recipe.id, result.id)
        assertEquals(recipe.name, result.name)
        assertEquals(recipe.description, result.description)
        assertEquals(recipe.cookingTime, result.cookingTime)
        assertEquals(recipe.servings, result.servings)
    }

    @Test
    fun `should throw EntityNotFoundException when recipe not found`() {
        // given
        val id = UUID.randomUUID()
        every { recipeRepository.findById(id) } returns Optional.empty()

        // when/then
        org.junit.jupiter.api.assertThrows<EntityNotFoundException> {
            recipeService.getById(id)
        }
    }

    @Test
    fun `should update recipe`() {
        // given
        val id = UUID.randomUUID()
        val recipe = recipe(id = id)
        val request = updateRecipeRequest()
        
        every { recipeRepository.findById(id) } returns Optional.of(recipe)
        every { recipeRepository.save(any()) } returns recipe

        // when
        val result = recipeService.update(id, request)

        // then
        assertEquals(request.name, result.name)
        assertEquals(request.description, result.description)
        assertEquals(request.cookingTime, result.cookingTime)
        assertEquals(request.servings, result.servings)
    }

    @Test
    fun `should get recipes by dish id`() {
        // given
        val dishId = UUID.randomUUID()
        val recipes = listOf(recipe(), recipe())
        
        every { recipeRepository.findByDishId(dishId) } returns recipes

        // when
        val result = recipeService.getByDishId(dishId)

        // then
        assertEquals(recipes.size, result.size)
        assertEquals(recipes[0].id, result[0].id)
        assertEquals(recipes[1].id, result[1].id)
    }
}
