package ru.cookbook.service

import org.springframework.stereotype.Service
import ru.cookbook.dto.dish.CreateDishRequest
import ru.cookbook.dto.dish.DishCardDto
import ru.cookbook.dto.dish.DishGridDto
import ru.cookbook.dto.dish.UpdateDishRequest
import ru.cookbook.mapper.toCardDto
import ru.cookbook.mapper.toEntity
import ru.cookbook.mapper.toGridDto
import ru.cookbook.mapper.update
import ru.cookbook.repository.DishRepository
import ru.cookbook.repository.ImageRepository
import java.util.UUID
import ru.cookbook.util.findByIdOrThrow

@Service
class DishService(
    private val dishRepository: DishRepository,
    private val imageRepository: ImageRepository
) {
    fun create(request: CreateDishRequest): UUID {
        val image = request.imageId?.let { imageId -> 
            imageRepository.findByIdOrThrow(imageId)
        }
        
        val dish = request.toEntity().apply { 
            this.image = image
        }
        
        return dishRepository.save(dish).id
    }

    fun getById(id: UUID): DishCardDto =
        dishRepository.findByIdOrThrow(id).toCardDto()

    fun update(id: UUID, request: UpdateDishRequest): DishCardDto {
        val dish = dishRepository.findByIdOrThrow(id)
        
        val image = request.imageId?.let { imageId -> 
            imageRepository.findByIdOrThrow(imageId)
        }
        
        dish.apply {
            update(request)
            this.image = image
        }
        
        return dishRepository.save(dish).toCardDto()
    }

    fun delete(id: UUID) {
        dishRepository.deleteById(id)
    }

    fun getAll(): List<DishGridDto> =
        dishRepository.findAll().map { it.toGridDto() }
}
