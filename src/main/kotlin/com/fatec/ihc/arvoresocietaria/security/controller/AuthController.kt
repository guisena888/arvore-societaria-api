package com.fatec.ihc.arvoresocietaria.security.controller

import com.fatec.ihc.arvoresocietaria.security.jwt.JwtUtils
import com.fatec.ihc.arvoresocietaria.security.models.ERole
import com.fatec.ihc.arvoresocietaria.security.models.Role
import com.fatec.ihc.arvoresocietaria.security.models.User
import com.fatec.ihc.arvoresocietaria.security.payload.JwtResponse
import com.fatec.ihc.arvoresocietaria.security.payload.LoginRequest
import com.fatec.ihc.arvoresocietaria.security.payload.MessageResponse
import com.fatec.ihc.arvoresocietaria.security.payload.SignupRequest
import com.fatec.ihc.arvoresocietaria.security.repository.RoleRepository
import com.fatec.ihc.arvoresocietaria.security.repository.UserRespository
import com.fatec.ihc.arvoresocietaria.security.service.UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.validation.Valid
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
class AuthController {
    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userRepository: UserRespository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var encoder: PasswordEncoder

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @PostMapping("/signin")
    fun authenticateUser(@RequestBody loginRequest: @Valid LoginRequest): ResponseEntity<*> {
        var authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))
        SecurityContextHolder.getContext().authentication = authentication
        var jwt = jwtUtils.generateJwtToken(authentication)
        var userDetails = authentication.getPrincipal() as UserDetailsImpl
        var roles = userDetails.authorities!!.stream()
                .map { item: GrantedAuthority -> item.authority }
                .collect(Collectors.toList())
        return ResponseEntity.ok(JwtResponse(jwt!!,
                userDetails.id!!,
                userDetails.username,
                userDetails.email!!,
                roles))
    }

    @PostMapping("/signup")
    fun registerUser(@RequestBody signUpRequest: @Valid SignupRequest): ResponseEntity<*>? {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse("Error: Username is already taken!"))
        }
        if (userRepository.existsByEmail(signUpRequest.email)) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse("Error: Email is already in use!"))
        }

        // Create new user's account
        var user = User(signUpRequest.username,
                signUpRequest.email,
                encoder.encode(signUpRequest.password))
        var strRoles: Set<String> = signUpRequest.role
        var roles: MutableSet<Role> = HashSet<Role>()
        if (strRoles == null) {
            val userRole: Role = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow({ RuntimeException("Error: Role is not found.") })
            roles.add(userRole)
        } else {
            strRoles.forEach(Consumer { role: String? ->
                when (role) {
                    "admin" -> {
                        var adminRole: Role = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow({ RuntimeException("Error: Role is not found.") })
                        roles.add(adminRole)
                    }
                    "mod" -> {
                        var modRole: Role = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow({ RuntimeException("Error: Role is not found.") })
                        roles.add(modRole)
                    }
                    else -> {
                        var userRole: Role = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow({ RuntimeException("Error: Role is not found.") })
                        roles.add(userRole)
                    }
                }
            })
        }
        user.roles = roles
        userRepository.save(user)
        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }
}