package cn.hillwind.wx.demo.web

import cn.hillwind.wx.demo.domain.Role
import cn.hillwind.wx.demo.repository.RoleRepository
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import javax.inject.Inject

@Controller("/")
class IndexController {

    @Inject
    lateinit var roleRepository: RoleRepository

    /**
     * 查询数量
     */
    @Get("/")
    fun index(): Role {
        val name = "R" + System.currentTimeMillis() % 1000
        println("\tname: " + name + ", len: " + name.length)
        var r = Role()
        r.name = name
        r.allPerm = true
        r.perms = "ROLE_ADMIN"
        r = roleRepository.save(r)
        println(roleRepository.count())
        return r
    }

}