package com.fatec.ihc.arvoresocietaria.security.payload

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class LoginRequest(
        @NotBlank
        var username: String,
        @NotBlank
        var password: String
)

data class SignupRequest(
        @NotBlank
        @Size(min= 3, max = 20)
        var username: String,

        @NotBlank
        @Size(max = 50)
        @Email
        var email: String,

        var role: Set<String>,

        @NotBlank
        @Size(min = 6, max = 40)
        var password: String
)

data class UserUpdateRequest(
        @NotBlank
        @Size(min= 3, max = 20)
        var username: String,

        @NotBlank
        @Size(max = 50)
        @Email
        var email: String,

        @NotBlank
        @Size(min = 6, max = 40)
        var password: String
)

data class JwtResponse(
        var token: String,
        var id: Long,
        val username: String,
        val email: String,
        var roles: List<String>
        ){
        var type: String = "Bearer"
}

data class MessageResponse( var message: String)