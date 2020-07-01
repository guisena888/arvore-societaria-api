package com.fatec.ihc.arvoresocietaria.controller

import com.fatec.ihc.arvoresocietaria.model.Empresa
import com.fatec.ihc.arvoresocietaria.repository.EmpresaRepository
import com.fatec.ihc.arvoresocietaria.security.models.User
import com.fatec.ihc.arvoresocietaria.security.service.UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/empresa")
class EmpresaController {

    @Autowired
    lateinit var empresaRepository: EmpresaRepository

    @GetMapping
    fun findAll(@RequestParam(required = false) name: String?): MutableIterable<Empresa> {
        return if(!name.isNullOrBlank()) {
            empresaRepository.findByNameContainingIgnoreCase(name)
        } else
            empresaRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<Empresa>{
        var empresaOptional = empresaRepository.findById(id)

        //Empresa não encontrada
        if(!empresaOptional.isPresent) return ResponseEntity.notFound().build()

        return ResponseEntity.ok(empresaOptional.get())
    }

    @GetMapping("/minhaempresa")
    fun findByLoggedUser(): MutableIterable<Empresa> {
        return empresaRepository.findByAdmin(findLoggedUser())
    }

    @PostMapping
    fun create(@RequestBody empresa: Empresa): Empresa {
        var loggedUser = findLoggedUser()
        empresa.admin = loggedUser
        return empresaRepository.save(empresa)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable("id") id: Long): ResponseEntity<Any>{
        var loggedUser = findLoggedUser()
        var empresaOptional = empresaRepository.findById(id)

        //Empresa não encontrada
        if(!empresaOptional.isPresent) return ResponseEntity.notFound().build()

        var empresa = empresaOptional.get()

        //Usuario nao autorizado
        if(empresa.admin?.id != loggedUser.id) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        empresaRepository.delete(empresa)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{id}")
    fun updateById(@PathVariable("id") id: Long,
                   @RequestBody empresaForm: Empresa): ResponseEntity<Empresa> {
        var loggedUser = findLoggedUser()
        var empresaOptional = empresaRepository.findById(id)

        //Empresa não encontrada
        if(!empresaOptional.isPresent) return ResponseEntity.notFound().build()

        var empresa = empresaOptional.get()

        //Usuario nao autorizado
        if(empresa.admin?.id != loggedUser.id) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        empresa.name = empresaForm.name
        empresa.cnpj = empresaForm.cnpj
        empresa.patrimonio = empresaForm.patrimonio
        empresaRepository.save(empresa)
        return ResponseEntity.ok(empresa)
    }

    fun findLoggedUser(): User {
        var userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl
        return User(userDetails)
    }

}