package ru.cookbook.controller

import java.util.UUID
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.cookbook.dto.dish.CreateDishRequest
import ru.cookbook.dto.dish.DishCardDto
import ru.cookbook.dto.dish.DishGridDto
import ru.cookbook.dto.dish.UpdateDishRequest
import ru.cookbook.service.DishService

@RestController
@RequestMapping("/api/v1/dishes")
class DishController(
    private val dishService: DishService
) {
    @PostMapping
    fun create(@RequestBody request: CreateDishRequest): UUID =
        dishService.create(request)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): DishCardDto =
        dishService.getById(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: UpdateDishRequest
    ): DishCardDto = dishService.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) =
        dishService.delete(id)

    @GetMapping
    fun getAll(): List<DishGridDto> =
        dishService.getAll()
}
