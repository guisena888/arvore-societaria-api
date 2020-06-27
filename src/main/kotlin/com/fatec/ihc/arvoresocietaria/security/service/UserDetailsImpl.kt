package com.fatec.ihc.arvoresocietaria.security.service

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fatec.ihc.arvoresocietaria.security.models.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors


class UserDetailsImpl : UserDetails {

    var id: Long? = null

    private var username: String? = null

    var email: String? = null

    @JsonIgnore
    private var password: String? = null

    private var authorities: Collection<GrantedAuthority>? = null

    constructor()

    constructor(id: Long?, username: String?, email: String?, password: String?,
                        authorities: Collection<GrantedAuthority>?) {
        this.id = id
        this.username = username
        this.email = email
        this.password = password
        this.authorities = authorities
    }
    companion object {
        fun build(user: User): UserDetailsImpl {
            var authorities: List<GrantedAuthority> = user.roles.stream()
                    .map { role -> SimpleGrantedAuthority(role.name?.name) }
                    .collect(Collectors.toList())
            return UserDetailsImpl(
                    user.id,
                    user.username,
                    user.email,
                    user.password,
                    authorities)
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority>? {
        return authorities
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getUsername(): String {
        return username!!
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

}