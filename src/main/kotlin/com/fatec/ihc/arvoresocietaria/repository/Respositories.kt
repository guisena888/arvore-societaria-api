package com.fatec.ihc.arvoresocietaria.repository

import com.fatec.ihc.arvoresocietaria.model.Empresa
import org.springframework.data.repository.CrudRepository

interface EmpresaRepository : CrudRepository<Empresa, Long> {
}