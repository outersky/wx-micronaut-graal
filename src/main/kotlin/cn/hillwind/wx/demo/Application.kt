package cn.hillwind.wx.demo

import io.micronaut.core.annotation.TypeHint
import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
        info = Info(
                title = "demo",
                version = "0.1"
        )
)
@TypeHint(value = [org.mariadb.jdbc.util.Options::class, org.mariadb.jdbc.Driver::class, org.dom4j.DocumentFactory::class],
        accessType = [TypeHint.AccessType.ALL_PUBLIC])
object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("wx.demo")
                .mainClass(Application.javaClass)
                .start()
    }
}
