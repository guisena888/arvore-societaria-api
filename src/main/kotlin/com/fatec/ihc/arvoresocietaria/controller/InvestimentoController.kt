package com.fatec.ihc.arvoresocietaria.controller

import com.fatec.ihc.arvoresocietaria.model.InvestidoDto
import com.fatec.ihc.arvoresocietaria.model.InvestidorDto
import com.fatec.ihc.arvoresocietaria.model.Investimento
import com.fatec.ihc.arvoresocietaria.model.InvestimentoForm
import com.fatec.ihc.arvoresocietaria.repository.EmpresaRepository
import com.fatec.ihc.arvoresocietaria.repository.InvestimentoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/investimento")
class InvestimentoController {

    @Autowired
    lateinit var investimentoRepository: InvestimentoRepository

    @Autowired
    lateinit var empresaRepository: EmpresaRepository

    @GetMapping("/empresa/{id}")
    fun findInvestidoresByEmpresaId(@PathVariable("id") id: Long): ResponseEntity<MutableIterable<InvestidorDto>>{
        var empresaOptional = empresaRepository.findById(id)
        if (!empresaOptional.isPresent) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(investimentoRepository.findByEmpresa(empresaOptional.get()).map { InvestidorDto(it) }.toMutableList())
    }

    @GetMapping("/investidor/{id}")
    fun findInvestidosByInvestidorId(@PathVariable("id") id: Long):  ResponseEntity<MutableIterable<InvestidoDto>>{
        var investidorOptional = empresaRepository.findById(id)
        if (!investidorOptional.isPresent) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(investimentoRepository.findByInvestidor(investidorOptional.get()).map { InvestidoDto(it) }.toMutableList())
    }

    @PostMapping
    fun createInvestimento(@RequestBody investimentoForm: InvestimentoForm): ResponseEntity<Investimento>{
        var empresaOptional = empresaRepository.findById(investimentoForm.idEmpresa)
        var investidorOptional = empresaRepository.findById(investimentoForm.idInvestidor)
        if (!empresaOptional.isPresent || !investidorOptional.isPresent) return ResponseEntity.notFound().build()

        var investimento = Investimento()
        investimento.empresa = empresaOptional.get()
        investimento.investidor = investidorOptional.get()

        var investimentoOptional = investimentoRepository.findByEmpresaAndInvestidor(empresaOptional.get(), investidorOptional.get())
        if (investimentoOptional.isPresent) {
            investimento = investimentoOptional.get()
        }

        investimento.qtdCotas += investimentoForm.qtdCotas
        investimentoRepository.save(investimento)

        return ResponseEntity.ok(investimento)
    }

    @DeleteMapping
    fun deleteInvestimento(@RequestBody investimentoForm: InvestimentoForm): ResponseEntity<Any>{
        var empresaOptional = empresaRepository.findById(investimentoForm.idEmpresa)
        var investidorOptional = empresaRepository.findById(investimentoForm.idInvestidor)
        if (!empresaOptional.isPresent || !investidorOptional.isPresent) return ResponseEntity.notFound().build()

        var empresa = empresaOptional.get()
        var investidor = investidorOptional.get()
        var investimentoOptional = investimentoRepository.findByEmpresaAndInvestidor(empresa, investidor)
        if (!investidorOptional.isPresent) return ResponseEntity.notFound().build()

        var investimento = investimentoOptional.get()
        if (investimento.qtdCotas == investimentoForm.qtdCotas){
            investimentoRepository.delete(investimento)
            return ResponseEntity.ok().build()
        }
        investimento.qtdCotas -= investimentoForm.qtdCotas
        investimentoRepository.save(investimento)
        return ResponseEntity.ok(investimento)
    }
}