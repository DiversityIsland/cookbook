package ru.cookbook

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.junit.jupiter.api.Assertions.assertEquals

@SpringBootTest
@ActiveProfiles("test")
class DatabaseConnectionTest {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun `should connect to database`() {
        val result = jdbcTemplate.queryForObject("SELECT 1", Int::class.java)
        assertEquals(1, result)
    }
}
