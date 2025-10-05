package ru.cookbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.cookbook.entity.Ingredient
import java.util.UUID

interface IngredientRepository : JpaRepository<Ingredient, UUID>
