package ru.cookbook.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.junit5.MockKExtension
import ru.cookbook.entity.Image
import ru.cookbook.repository.ImageRepository
import ru.cookbook.util.image
import ru.cookbook.util.imageUploadRequest
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
class ImageServiceTest(
    @MockK private val imageRepository: ImageRepository
) {
    private val imageService = ImageService(imageRepository)

    @Test
    fun `should upload image`() {
        // given
        val request = imageUploadRequest()
        val imageSlot = slot<Image>()
        val savedImage = image(
            data = request.data,
            contentType = request.contentType,
            size = request.data.size.toLong()
        )
        
        every { imageRepository.save(capture(imageSlot)) } returns savedImage

        // when
        imageService.upload(request)

        // then
        verify { imageRepository.save(any()) }
        assertEquals(request.data.contentEquals(imageSlot.captured.data), true)
        assertEquals(request.contentType, imageSlot.captured.contentType)
        assertEquals(request.data.size.toLong(), imageSlot.captured.size)
    }

    @Test
    fun `should get image by id`() {
        // given
        val id = UUID.randomUUID()
        val image = image(id = id)
        
        every { imageRepository.findById(id) } returns Optional.of(image)

        // when
        val result = imageService.getById(id)

        // then
        assertEquals(image.id, result.id)
        assertEquals(image.contentType, result.contentType)
        assertEquals(image.size, result.size)
    }

    @Test
    fun `should get image data`() {
        // given
        val id = UUID.randomUUID()
        val image = image(id = id)
        
        every { imageRepository.findById(id) } returns Optional.of(image)

        // when
        val result = imageService.getData(id)

        // then
        assertEquals(image.data.contentEquals(result), true)
    }

    @Test
    fun `should throw EntityNotFoundException when image not found`() {
        // given
        val id = UUID.randomUUID()
        every { imageRepository.findById(id) } returns Optional.empty()

        // when/then
        org.junit.jupiter.api.assertThrows<EntityNotFoundException> {
            imageService.getById(id)
        }
    }
}
