package com.fatec.ihc.arvoresocietaria.model

import java.math.BigDecimal

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
    var qtdCotas: Long,
    var precoCota: BigDecimal
)