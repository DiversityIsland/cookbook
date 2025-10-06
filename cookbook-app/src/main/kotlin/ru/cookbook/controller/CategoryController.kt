package ru.cookbook.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.cookbook.dto.category.CategoryCardDto
import ru.cookbook.dto.category.CategoryGridDto
import ru.cookbook.dto.category.CreateCategoryRequest
import ru.cookbook.dto.category.UpdateCategoryRequest
import ru.cookbook.service.CategoryService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(
    private val categoryService: CategoryService
) {
    @PostMapping
    fun create(@RequestBody request: CreateCategoryRequest): UUID =
        categoryService.create(request)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): CategoryCardDto =
        categoryService.getById(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: UpdateCategoryRequest
    ): CategoryCardDto = categoryService.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) =
        categoryService.delete(id)

    @GetMapping
    fun getAll(): List<CategoryGridDto> =
        categoryService.getAll()
}
