package ru.cookbook.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import org.hibernate.Hibernate
import java.time.OffsetDateTime
import java.util.UUID

@Entity
open class Dish(
    @Id
    open val id: UUID = UUID.randomUUID(),
    
    open var name: String,
    
    @Column(columnDefinition = "TEXT")
    open var description: String,
    
    
    @OneToOne(cascade = [CascadeType.ALL])
    open var image: Image? = null,
    
    open val createdAt: OffsetDateTime = OffsetDateTime.now(),
    open var updatedAt: OffsetDateTime = OffsetDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Dish
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = "Dish(id=$id, name='$name')"

    @OneToMany(mappedBy = "dish", cascade = [CascadeType.ALL])
    open val recipes: MutableList<Recipe> = mutableListOf()
    
    @OneToMany(mappedBy = "dish", cascade = [CascadeType.ALL])
    open val categoryLinks: MutableSet<CategoryDish> = mutableSetOf()
}
