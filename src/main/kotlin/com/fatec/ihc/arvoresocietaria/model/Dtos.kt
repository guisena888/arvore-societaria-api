package com.fatec.ihc.arvoresocietaria.model

import java.math.BigDecimal
import javax.persistence.*

data class EmpresaDto (
    var id: Long? = null,
    var name: String,
    var cnpj: String,
    var investidores: List<Empresa> = mutableListOf<Empresa>()

)

data class EmpresaInvestidorDto (
        var idEmpresa: Long,
        var idInvestidor: Long
)

data class InvestimentoForm (
    var idEmpresa: Long,
    var idInvestidor: Long,
    var qtdCotas: Long
)

class InvestidorDto {

    var investidor: Empresa? = null

    var qtdCotas: Long = 0

    constructor(investimento: Investimento){
        this.investidor = investimento.investidor
        this.qtdCotas = investimento.qtdCotas
    }

}

class InvestidoDto {

    var investido: Empresa? = null

    var qtdCotas: Long = 0

    constructor(investimento: Investimento){
        this.investido = investimento.empresa
        this.qtdCotas = investimento.qtdCotas
    }

}