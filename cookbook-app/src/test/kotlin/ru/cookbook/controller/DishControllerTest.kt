package ru.cookbook.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import jakarta.persistence.EntityNotFoundException
import java.util.UUID
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.cookbook.service.DishService
import ru.cookbook.util.createDishRequest

@WebMvcTest(DishController::class)
@ActiveProfiles("test")
class DishControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var dishService: DishService

    private val mapper = jacksonObjectMapper()

    @Test
    fun `should create dish`() {
        // given
        val request = createDishRequest()
        val dishId = UUID.randomUUID()
        
        every { dishService.create(request) } returns dishId

        // when/then
        mockMvc.perform(
            post("/api/v1/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 404 when dish not found`() {
        // given
        val id = UUID.randomUUID()
        every { dishService.getById(id) } throws EntityNotFoundException()

        // when/then
        mockMvc.perform(get("/api/v1/dishes/$id"))
            .andExpect(status().isNotFound)
    }
}