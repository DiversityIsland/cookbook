package ru.cookbook.service

import org.springframework.stereotype.Service
import ru.cookbook.dto.image.ImageDto
import ru.cookbook.dto.image.ImageUploadRequest
import ru.cookbook.entity.Image
import ru.cookbook.mapper.toDto
import ru.cookbook.mapper.toEntity
import ru.cookbook.repository.ImageRepository
import ru.cookbook.util.findByIdOrThrow
import java.util.UUID

@Service
class ImageService(
    private val imageRepository: ImageRepository
) {
    fun upload(request: ImageUploadRequest): UUID {
        val image = request.toEntity()
        return imageRepository.save(image).id
    }

    fun getById(id: UUID): ImageDto =
        imageRepository.findByIdOrThrow(id).toDto()

    fun getData(id: UUID): ByteArray =
        imageRepository.findByIdOrThrow(id).data

    fun delete(id: UUID) {
        imageRepository.deleteById(id)
    }
}
