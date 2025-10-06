package ru.cookbook.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.junit5.MockKExtension
import ru.cookbook.entity.Category
import ru.cookbook.repository.CategoryRepository
import ru.cookbook.util.category
import ru.cookbook.util.createCategoryRequest
import ru.cookbook.util.updateCategoryRequest
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
class CategoryServiceTest(
    @MockK private val categoryRepository: CategoryRepository
) {
    private val categoryService = CategoryService(categoryRepository)

    @Test
    fun `should create category`() {
        // given
        val request = createCategoryRequest()
        val categorySlot = slot<Category>()
        val savedCategory = category(
            name = request.name,
            description = request.description
        )
        
        every { categoryRepository.save(capture(categorySlot)) } returns savedCategory

        // when
        categoryService.create(request)

        // then
        verify { categoryRepository.save(any()) }
        assertEquals(request.name, categorySlot.captured.name)
        assertEquals(request.description, categorySlot.captured.description)
    }

    @Test
    fun `should get category by id`() {
        // given
        val id = UUID.randomUUID()
        val category = category(id = id)
        
        every { categoryRepository.findById(id) } returns Optional.of(category)

        // when
        val result = categoryService.getById(id)

        // then
        assertEquals(category.id, result.id)
        assertEquals(category.name, result.name)
        assertEquals(category.description, result.description)
    }

    @Test
    fun `should throw EntityNotFoundException when category not found`() {
        // given
        val id = UUID.randomUUID()
        every { categoryRepository.findById(id) } returns Optional.empty()

        // when/then
        org.junit.jupiter.api.assertThrows<EntityNotFoundException> {
            categoryService.getById(id)
        }
    }

    @Test
    fun `should update category`() {
        // given
        val id = UUID.randomUUID()
        val category = category(id = id)
        val request = updateCategoryRequest()
        
        every { categoryRepository.findById(id) } returns Optional.of(category)
        every { categoryRepository.save(any()) } returns category

        // when
        val result = categoryService.update(id, request)

        // then
        assertEquals(request.name, result.name)
        assertEquals(request.description, result.description)
    }
}
