package ru.cookbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.cookbook.entity.Category
import java.util.UUID

interface CategoryRepository : JpaRepository<Category, UUID>
