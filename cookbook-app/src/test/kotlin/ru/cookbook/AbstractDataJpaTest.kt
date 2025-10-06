package ru.cookbook

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import ru.cookbook.entity.Dish
import ru.cookbook.entity.Image
import ru.cookbook.repository.DishRepository
import ru.cookbook.repository.ImageRepository

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
abstract class AbstractDataJpaTest {
    @Autowired
    lateinit var dishRepository: DishRepository

    @Autowired
    lateinit var imageRepository: ImageRepository

    @PersistenceContext
    lateinit var em: EntityManager

    fun persistDish(dish: Dish): Dish {
        val saved = dishRepository.save(dish)
        em.flush()
        em.clear()
        return saved
    }

    fun persistImage(image: Image): Image {
        val saved = imageRepository.save(image)
        em.flush()
        em.clear()
        return saved
    }
}
