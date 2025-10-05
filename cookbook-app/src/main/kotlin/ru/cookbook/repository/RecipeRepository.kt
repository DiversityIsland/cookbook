package ru.cookbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.cookbook.entity.Recipe
import java.util.UUID

interface RecipeRepository : JpaRepository<Recipe, UUID>
