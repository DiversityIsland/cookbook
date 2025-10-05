package ru.cookbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.cookbook.entity.CookingStep
import java.util.UUID

interface CookingStepRepository : JpaRepository<CookingStep, UUID>
