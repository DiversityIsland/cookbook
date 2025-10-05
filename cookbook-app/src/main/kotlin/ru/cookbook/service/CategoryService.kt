package ru.cookbook.service

import org.springframework.stereotype.Service
import ru.cookbook.repository.CategoryRepository
import java.util.UUID

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
)
