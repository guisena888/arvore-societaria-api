package com.fatec.ihc.arvoresocietaria.security.jwt

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fatec.ihc.arvoresocietaria.security.service.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthTokenFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @Autowired
    lateinit var userDetailsService: UserDetailsServiceImpl

    private val log: Logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        return if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth.substring(7, headerAuth.length)
        } else null
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val jwt = parseJwt(request)
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                var username: String? = jwtUtils.getUserNameFromJwtToken(jwt)
                var userDetails = userDetailsService!!.loadUserByUsername(username)
                var authentication = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails!!.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            log.error("Cannot set user authentication: {}", e)
        }
        filterChain.doFilter(request, response)
    }
}