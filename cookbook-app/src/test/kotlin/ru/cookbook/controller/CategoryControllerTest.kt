package ru.cookbook.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.cookbook.service.CategoryService
import ru.cookbook.util.category
import ru.cookbook.util.createCategoryRequest
import ru.cookbook.util.updateCategoryRequest
import ru.cookbook.mapper.toCardDto
import java.util.UUID

@WebMvcTest(CategoryController::class)
@ActiveProfiles("test")
class CategoryControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var categoryService: CategoryService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should create category`() {
        // given
        val request = createCategoryRequest()
        val categoryId = UUID.randomUUID()
        
        every { categoryService.create(request) } returns categoryId

        // when/then
        mockMvc.perform(
            post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should update category`() {
        // given
        val id = UUID.randomUUID()
        val request = updateCategoryRequest()
        
        every { categoryService.update(id, request) } returns category(id = id).toCardDto()

        // when/then
        mockMvc.perform(
            put("/api/v1/categories/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 404 when category not found`() {
        // given
        val id = UUID.randomUUID()
        every { categoryService.getById(id) } throws EntityNotFoundException()

        // when/then
        mockMvc.perform(get("/api/v1/categories/$id"))
            .andExpect(status().isNotFound)
    }
}
