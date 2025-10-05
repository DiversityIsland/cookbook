package ru.cookbook.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import java.time.OffsetDateTime
import java.util.UUID
import org.hibernate.Hibernate

@Entity
open class Category(
    @Id
    open val id: UUID = UUID.randomUUID(),
    
    open val name: String,
    
    @Column(columnDefinition = "TEXT")
    open val description: String?,
    
    
    open val createdAt: OffsetDateTime = OffsetDateTime.now(),
    open var updatedAt: OffsetDateTime = OffsetDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Category
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = "Category(id=$id, name='$name')"

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL])
    open val dishLinks: MutableSet<CategoryDish> = mutableSetOf()
}
