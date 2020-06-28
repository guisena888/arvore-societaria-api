package com.fatec.ihc.arvoresocietaria.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fatec.ihc.arvoresocietaria.security.models.User
import java.math.BigDecimal
import javax.persistence.*

@Entity
class Empresa(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(unique=true)
        var name: String,

        @Column(unique=true)
        var cnpj: String,
        var patrimonio: BigDecimal,
        var qtdCotasTotal: Long,

        @ManyToOne
        @JsonIgnore
        var admin: User?
)


@Entity
class Investimento(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne
        var empresa: Empresa,

        @ManyToOne
        var investidor: Empresa,

        var precoMedio: BigDecimal,
        var qtdCotas: Long
)