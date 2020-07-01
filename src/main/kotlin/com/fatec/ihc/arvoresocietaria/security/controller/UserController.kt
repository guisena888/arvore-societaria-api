package com.fatec.ihc.arvoresocietaria.security.controller

import com.fatec.ihc.arvoresocietaria.security.payload.MessageResponse
import com.fatec.ihc.arvoresocietaria.security.payload.UserUpdateRequest
import com.fatec.ihc.arvoresocietaria.security.repository.UserRespository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    lateinit var userRespository: UserRespository

    @Autowired
    lateinit var encoder: PasswordEncoder

    @PutMapping("/{id}")
    fun updateUser(@PathVariable("id") id: Long,
            @RequestBody userUpdateRequest: UserUpdateRequest) : ResponseEntity<Any>{
        var userOptional = userRespository.findById(id)
        if (!userOptional.isPresent) return ResponseEntity.notFound().build()

        var user = userOptional.get()
        user.username = userUpdateRequest.username
        user.email = userUpdateRequest.email
        user.password = encoder.encode(userUpdateRequest.password)
        userRespository.save(user)
        return ResponseEntity.ok(user)
    }

    @DeleteMapping
    fun deleteUser(@PathVariable("id") id: Long) : ResponseEntity<Any>{
        var userOptional = userRespository.findById(id)
        if (!userOptional.isPresent) return ResponseEntity.notFound().build()

        var user = userOptional.get()
        userRespository.delete(user)
        return ResponseEntity.ok().build()
    }
}