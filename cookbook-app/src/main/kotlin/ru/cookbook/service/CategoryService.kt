package ru.cookbook.service

import org.springframework.stereotype.Service
import ru.cookbook.dto.category.CategoryCardDto
import ru.cookbook.dto.category.CategoryGridDto
import ru.cookbook.dto.category.CreateCategoryRequest
import ru.cookbook.dto.category.UpdateCategoryRequest
import ru.cookbook.mapper.toCardDto
import ru.cookbook.mapper.toEntity
import ru.cookbook.mapper.toGridDto
import ru.cookbook.mapper.update
import ru.cookbook.repository.CategoryRepository
import ru.cookbook.util.findByIdOrThrow
import java.util.UUID

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
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
}
