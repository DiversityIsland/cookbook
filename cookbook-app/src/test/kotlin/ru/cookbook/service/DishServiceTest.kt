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
import ru.cookbook.entity.Dish
import ru.cookbook.repository.DishRepository
import ru.cookbook.repository.ImageRepository
import ru.cookbook.util.createDishRequest
import ru.cookbook.util.dish
import java.util.UUID

@ExtendWith(MockKExtension::class)
class DishServiceTest(
    @MockK private val dishRepository: DishRepository,
    @MockK private val imageRepository: ImageRepository
) {
    private val dishService = DishService(dishRepository, imageRepository)

    @Test
    fun `should create dish`() {
        // given
        val request = createDishRequest()
        val dishSlot = slot<Dish>()
        val savedDish = dish(
            name = request.name,
            description = request.description
        )
        
        every { dishRepository.save(capture(dishSlot)) } returns savedDish

        // when
        val result = dishService.create(request)

        // then
        verify { dishRepository.save(any()) }
        assertEquals(request.name, dishSlot.captured.name)
        assertEquals(request.description, dishSlot.captured.description)
    }

    @Test
    fun `should get dish by id`() {
        // given
        val id = UUID.randomUUID()
        val dish = dish(id = id)
        
        every { dishRepository.findById(id) } returns java.util.Optional.of(dish)

        // when
        val result = dishService.getById(id)

        // then
        assertEquals(dish.id, result.id)
        assertEquals(dish.name, result.name)
    }

    @Test
    fun `should throw EntityNotFoundException when dish not found`() {
        // given
        val id = UUID.randomUUID()
        every { dishRepository.findById(id) } returns java.util.Optional.empty()

        // when/then
        org.junit.jupiter.api.assertThrows<EntityNotFoundException> {
            dishService.getById(id)
        }
    }
}