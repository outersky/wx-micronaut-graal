package cn.hillwind.wx.demo.domain

import io.micronaut.data.annotation.AutoPopulated
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import java.util.*
import javax.persistence.*

@Entity
data class Manufacturer(
        @Id
        @GeneratedValue
        var id: Long?,
        val name: String
)

@Entity
data class Role(
        @Id
        @GeneratedValue
        var id: Long? = null,
        var name: String = "",
        var description: String? = null,
        @DateCreated
        var ct: Long? = null,
        @AutoPopulated
        var uuid: UUID? = null,

        @DateUpdated
        var ut: Long? = null,

        var nt: String? = null,

        var perms: String? = null,

        //是否拥有所有权限，初始化的admin应该为true
        @Column
        var allPerm: Boolean = false
)