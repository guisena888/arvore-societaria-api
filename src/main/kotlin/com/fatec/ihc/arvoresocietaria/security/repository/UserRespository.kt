package com.fatec.ihc.arvoresocietaria.security.repository

import com.fatec.ihc.arvoresocietaria.security.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRespository: JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}