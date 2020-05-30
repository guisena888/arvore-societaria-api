package com.fatec.ihc.arvoresocietaria.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Empresa(
        @Id @GeneratedValue var id: Long? = null,
        var name: String,
        var cnpj: String

)