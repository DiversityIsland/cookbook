package ru.cookbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.cookbook.entity.Dish
import java.util.UUID

interface DishRepository : JpaRepository<Dish, UUID>
