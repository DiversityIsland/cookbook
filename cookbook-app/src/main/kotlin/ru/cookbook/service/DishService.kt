package ru.cookbook.service

import org.springframework.stereotype.Service
import ru.cookbook.repository.DishRepository
import ru.cookbook.repository.ImageRepository
import java.util.UUID

@Service
class DishService(
    private val dishRepository: DishRepository,
    private val imageRepository: ImageRepository
)
