package ru.cookbook.util

import jakarta.persistence.EntityNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun entityNotFound(
    name: String?,
    id: Any?,
): Nothing = throw EntityNotFoundException("${name ?: "Entity"} with $id not found")

inline fun <reified T, F> JpaRepository<T, F>.findByIdOrThrow(id: F): T =
    findByIdOrNull(id) ?: entityNotFound(T::class.simpleName, id)
