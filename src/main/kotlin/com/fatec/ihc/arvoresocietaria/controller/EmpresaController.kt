package com.fatec.ihc.arvoresocietaria.controller

import com.fatec.ihc.arvoresocietaria.model.Empresa
import com.fatec.ihc.arvoresocietaria.repository.EmpresaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/empresa")
class EmpresaController {

    @Autowired
    lateinit var empresaRepository: EmpresaRepository

    @GetMapping
    fun findAll(): MutableIterable<Empresa> {
        return empresaRepository.findAll()
    }
}