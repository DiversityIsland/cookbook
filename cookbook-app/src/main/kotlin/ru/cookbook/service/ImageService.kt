package ru.cookbook.service

import org.springframework.stereotype.Service
import ru.cookbook.repository.ImageRepository
import java.util.UUID

@Service
class ImageService(
    private val imageRepository: ImageRepository
)
