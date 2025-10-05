package ru.cookbook.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import org.hibernate.Hibernate
import java.time.Duration
import java.time.OffsetDateTime
import java.util.UUID

@Entity
open class Recipe(
    @Id
    open val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    open val dish: Dish,
    
    open val name: String,
    
    @Column(columnDefinition = "TEXT")
    open val description: String,
    
    open val cookingTime: Duration,
    open val servings: Int,
    
    open val createdAt: OffsetDateTime = OffsetDateTime.now(),
    open var updatedAt: OffsetDateTime = OffsetDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Recipe
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = "Recipe(id=$id, name='$name', dish.name='${dish.name}')"

    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL])
    open val ingredients: MutableList<Ingredient> = mutableListOf()
    
    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL])
    open val steps: MutableList<CookingStep> = mutableListOf()
}
