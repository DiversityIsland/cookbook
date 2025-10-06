package ru.cookbook.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.cookbook.service.ImageService
import ru.cookbook.util.image
import ru.cookbook.util.imageUploadRequest
import java.util.UUID
import ru.cookbook.mapper.toDto

@WebMvcTest(ImageController::class)
@ActiveProfiles("test")
class ImageControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var imageService: ImageService

    @Test
    fun `should upload image`() {
        // given
        val imageId = UUID.randomUUID()
        val file = MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "test image data".toByteArray()
        )
        
        every { 
            imageService.upload(match { 
                it.contentType == file.contentType && 
                it.data.contentEquals(file.bytes) 
            }) 
        } returns imageId

        // when/then
        mockMvc.perform(
            multipart("/api/v1/images")
                .file(file)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should get image data`() {
        // given
        val id = UUID.randomUUID()
        val image = image(id = id)
        
        every { imageService.getById(id) } returns image.toDto()
        every { imageService.getData(id) } returns image.data

        // when/then
        mockMvc.perform(get("/api/v1/images/$id/data"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(image.contentType))
            .andExpect(content().bytes(image.data))
    }

    @Test
    fun `should return 404 when image not found`() {
        // given
        val id = UUID.randomUUID()
        every { imageService.getById(id) } throws EntityNotFoundException()

        // when/then
        mockMvc.perform(get("/api/v1/images/$id"))
            .andExpect(status().isNotFound)
    }
}
