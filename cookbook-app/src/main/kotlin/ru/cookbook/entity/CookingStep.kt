package ru.cookbook.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import java.time.Duration
import java.util.UUID
import org.hibernate.Hibernate

@Entity
open class CookingStep(
    @Id
    open val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    open val recipe: Recipe,
    
    open val stepNumber: Int,
    
    @Column(columnDefinition = "TEXT")
    open val description: String,
    
    open val estimatedTime: Duration?,
    
    @OneToOne(cascade = [CascadeType.ALL])
    open var image: Image? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CookingStep
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = "CookingStep(id=$id, stepNumber=$stepNumber)"
}
