package ru.cookbook.service

import org.springframework.stereotype.Service
import ru.cookbook.dto.category.CategoryCardDto
import ru.cookbook.dto.category.CategoryGridDto
import ru.cookbook.dto.category.CreateCategoryRequest
import ru.cookbook.dto.category.UpdateCategoryRequest
import ru.cookbook.dto.dish.DishGridDto
import ru.cookbook.entity.CategoryDish
import ru.cookbook.mapper.toCardDto
import ru.cookbook.mapper.toEntity
import ru.cookbook.mapper.toGridDto
import ru.cookbook.mapper.update
import ru.cookbook.repository.CategoryDishRepository
import ru.cookbook.repository.CategoryRepository
import ru.cookbook.repository.DishRepository
import ru.cookbook.util.findByIdOrThrow
import java.util.UUID

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val dishRepository: DishRepository,
    private val categoryDishRepository: CategoryDishRepository
) {
    fun create(request: CreateCategoryRequest): UUID {
        val category = request.toEntity()
        return categoryRepository.save(category).id
    }

    fun getById(id: UUID): CategoryCardDto =
        categoryRepository.findByIdOrThrow(id).toCardDto()

    fun update(id: UUID, request: UpdateCategoryRequest): CategoryCardDto {
        val category = categoryRepository.findByIdOrThrow(id)
        category.update(request)
        return categoryRepository.save(category).toCardDto()
    }

    fun delete(id: UUID) {
        categoryRepository.deleteById(id)
    }

    fun getAll(): List<CategoryGridDto> =
        categoryRepository.findAll().map { it.toGridDto() }

    fun getDishes(id: UUID): List<DishGridDto> {
        val category = categoryRepository.findByIdOrThrow(id)
        return category.dishLinks.map { it.dish.toGridDto() }
    }

    fun addDish(categoryId: UUID, dishId: UUID) {
        val category = categoryRepository.findByIdOrThrow(categoryId)
        val dish = dishRepository.findByIdOrThrow(dishId)
        
        val categoryDish = CategoryDish(
            category = category,
            dish = dish
        )
        
        categoryDishRepository.save(categoryDish)
    }
}
