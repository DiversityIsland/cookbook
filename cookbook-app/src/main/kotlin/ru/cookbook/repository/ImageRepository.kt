package ru.cookbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.cookbook.entity.Image
import java.util.UUID

interface ImageRepository : JpaRepository<Image, UUID>
