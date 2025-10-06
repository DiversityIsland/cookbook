package ru.cookbook.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.cookbook.config.JacksonConfig
import ru.cookbook.service.RecipeService
import ru.cookbook.util.createRecipeRequest
import ru.cookbook.util.recipe
import ru.cookbook.util.updateRecipeRequest
import java.util.UUID
import ru.cookbook.mapper.toCardDto

@WebMvcTest(RecipeController::class)
@Import(JacksonConfig::class)
@ActiveProfiles("test")
class RecipeControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var recipeService: RecipeService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should create recipe`() {
        // given
        val dishId = UUID.randomUUID()
        val request = createRecipeRequest()
        val recipeId = UUID.randomUUID()
        
        every { recipeService.create(dishId, request) } returns recipeId

        // when/then
        mockMvc.perform(
            post("/api/v1/recipes/dish/$dishId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should update recipe`() {
        // given
        val id = UUID.randomUUID()
        val request = updateRecipeRequest()
        
        every { recipeService.update(id, request) } returns recipe(id = id).toCardDto()

        // when/then
        mockMvc.perform(
            put("/api/v1/recipes/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 404 when recipe not found`() {
        // given
        val id = UUID.randomUUID()
        every { recipeService.getById(id) } throws EntityNotFoundException()

        // when/then
        mockMvc.perform(get("/api/v1/recipes/$id"))
            .andExpect(status().isNotFound)
    }
}