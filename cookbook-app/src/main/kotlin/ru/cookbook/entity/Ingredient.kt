package ru.cookbook.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import ru.cookbook.enums.MeasurementUnit
import java.math.BigDecimal
import java.util.UUID
import org.hibernate.Hibernate

@Entity
open class Ingredient(
    @Id
    open val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    open val recipe: Recipe,
    
    open val name: String,
    open val amount: BigDecimal,
    
    @Enumerated(EnumType.STRING)
    open val unit: MeasurementUnit,
    
    open val isOptional: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null ||
            Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Ingredient
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = "Ingredient(id=$id, name='$name', amount=$amount $unit)"
}
