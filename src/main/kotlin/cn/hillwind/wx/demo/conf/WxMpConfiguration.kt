package cn.hillwind.wx.demo.conf

import cn.hillwind.wx.demo.util.JsonHelper
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Factory
import io.micronaut.core.annotation.Introspected
import me.chanjar.weixin.mp.api.WxMpMessageRouter
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl
import me.chanjar.weixin.mp.config.WxMpConfigStorage
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl
import java.util.stream.Collectors
import javax.inject.Inject

/**
 * wechat mp configuration
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Factory
class WxMpConfiguration {

    @Inject
    lateinit var properties: WxMpProperties

    @Bean
    fun wxMpService(): WxMpService { // 代码里 getConfigs()处报错的同学，请注意仔细阅读项目说明，你的IDE需要引入lombok插件！！！！
        val configs: List<WxMpProperties.MpConfig> = properties.configs
                ?: throw RuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！")
//        println("wx config:")
//        println(configs.joinToString(","){ it.toString() })
        val service: WxMpService = WxMpServiceImpl()
        service.setMultiConfigStorages(configs
                .stream().map { a: WxMpProperties.MpConfig ->
                    val configStorage = WxMpDefaultConfigImpl()
                    configStorage.appId = a.appId
                    configStorage.secret = a.secret
                    configStorage.token = a.token
                    configStorage.aesKey = a.aesKey
                    configStorage
                }.collect(Collectors.toMap({ obj: WxMpDefaultConfigImpl -> obj.appId }, { a: WxMpDefaultConfigImpl? -> a }) { o: WxMpConfigStorage?, _: WxMpConfigStorage? -> o }))
        return service
    }

    @Bean
    fun messageRouter(wxMpService: WxMpService?): WxMpMessageRouter {
        val newRouter = WxMpMessageRouter(wxMpService)
        return newRouter
    }
}

/**
 * wechat mp properties
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@ConfigurationProperties("wx.mp")
@Introspected
class WxMpProperties {

    var configs: List<MpConfig>? = null

    @Introspected
    class MpConfig {
        /**
         * 设置微信公众号的appid
         */
        var appId: String? = null
        /**
         * 设置微信公众号的app secret
         */
        var secret: String? = null
        /**
         * 设置微信公众号的token
         */
        var token: String? = null
        /**
         * 设置微信公众号的EncodingAESKey
         */
        var aesKey: String? = null

        override fun toString(): String {
            return JsonHelper.toJson(this)
        }

    }

    override fun toString(): String {
        return JsonHelper.toJson(this)
    }
}