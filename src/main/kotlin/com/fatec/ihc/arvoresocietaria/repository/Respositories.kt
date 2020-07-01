package com.fatec.ihc.arvoresocietaria.repository

import com.fatec.ihc.arvoresocietaria.model.Empresa
import com.fatec.ihc.arvoresocietaria.model.Investimento
import com.fatec.ihc.arvoresocietaria.security.models.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface EmpresaRepository : CrudRepository<Empresa, Long> {

//    @Query("SELECT I FROM Empresa e JOIN e.investidores I where e.id = :id")
//    fun findInvestidors(@Param("id") id: Long): MutableIterable<Empresa>

    fun findByNameContainingIgnoreCase(name: String): MutableIterable<Empresa>
    fun findByAdmin(user: User): MutableIterable<Empresa>
    fun findByCnpj(cnpjEmpresa: String): Empresa
}

interface InvestimentoRepository : CrudRepository<Investimento, Long> {
    fun findByEmpresa(empresa: Empresa): MutableIterable<Investimento>
    fun findByInvestidor(investidor: Empresa): MutableIterable<Investimento>
    fun findByEmpresaAndInvestidor(empresa: Empresa, investidor: Empresa): Optional<Investimento>
}
