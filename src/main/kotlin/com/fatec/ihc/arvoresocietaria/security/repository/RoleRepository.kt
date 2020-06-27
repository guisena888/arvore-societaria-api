package com.fatec.ihc.arvoresocietaria.security.repository

import com.fatec.ihc.arvoresocietaria.security.models.ERole
import com.fatec.ihc.arvoresocietaria.security.models.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: ERole): Optional<Role>
}