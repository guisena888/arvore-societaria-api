package com.fatec.ihc.arvoresocietaria.controller

import com.fatec.ihc.arvoresocietaria.model.InvestimentoForm
import com.fatec.ihc.arvoresocietaria.repository.EmpresaRepository
import com.fatec.ihc.arvoresocietaria.repository.InvestimentoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController("/investimento")
class InvestimentoController {

    @Autowired
    lateinit var investimentoRepository: InvestimentoRepository

    @Autowired
    lateinit var empresaRepository: EmpresaRepository

    @GetMapping("/{id}/empresa")
    fun findInvestidoresByEmpresaId(){

    }

    @GetMapping("/investidor/{id}")
    fun findInvestidosByInvestidorId(){

    }

    @PostMapping
    fun createInvestimento(@RequestBody investimentoForm: InvestimentoForm){

    }

    @PutMapping
    fun updateInvestimento(@RequestBody investimentoForm: InvestimentoForm){

    }

    @DeleteMapping
    fun deleteInvestimento(@RequestBody investimentoForm: InvestimentoForm){

    }
}