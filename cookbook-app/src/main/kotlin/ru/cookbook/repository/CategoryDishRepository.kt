package ru.cookbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.cookbook.entity.CategoryDish
import java.util.UUID

interface CategoryDishRepository : JpaRepository<CategoryDish, UUID>
