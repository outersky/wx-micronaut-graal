package cn.hillwind.wx.demo.repository

import cn.hillwind.wx.demo.domain.Role
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.*

@JdbcRepository(dialect = Dialect.MYSQL)
interface RoleRepository : CrudRepository<Role, Long> {
    fun find(name: String): Optional<Role>
    fun list(pageable: Pageable): Page<Role>
}