package ru.cookbook.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Lob
import org.hibernate.Hibernate
import java.time.OffsetDateTime
import java.util.UUID

@Entity
open class Image(
    @Id
    open val id: UUID = UUID.randomUUID(),
    
    @Lob
    @Column(columnDefinition = "bytea")
    open val data: ByteArray,
    
    open val contentType: String,
    open val size: Long,
    
    open val createdAt: OffsetDateTime = OffsetDateTime.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Image
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String = "Image(id=$id, contentType='$contentType', size=$size)"
}
