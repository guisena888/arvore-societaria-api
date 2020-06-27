package com.fatec.ihc.arvoresocietaria.security.service

import com.fatec.ihc.arvoresocietaria.security.models.User
import com.fatec.ihc.arvoresocietaria.security.repository.UserRespository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserDetailsServiceImpl : UserDetailsService{

    @Autowired
    lateinit var userRepository: UserRespository

    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails? {
        val user: User = userRepository.findByUsername(username!!)
                .orElseThrow { UsernameNotFoundException("User Not Found with username: $username") }

        return UserDetailsImpl.build(user)
    }
}