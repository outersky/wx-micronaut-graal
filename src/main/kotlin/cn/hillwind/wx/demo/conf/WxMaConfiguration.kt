package cn.hillwind.wx.demo.conf

import cn.binarywang.wx.miniapp.api.WxMaService
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl
import cn.hillwind.wx.demo.util.JsonHelper
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Factory
import io.micronaut.core.annotation.Introspected
import me.chanjar.weixin.mp.api.WxMpMessageRouter
import me.chanjar.weixin.mp.api.WxMpService
import javax.inject.Inject

/**
 * wechat mp configuration
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Factory
class WxMaConfiguration {

    @Inject
    lateinit var properties: WxMaProperties

    @Bean
    fun wxMaService(): WxMaService { // 代码里 getConfigs()处报错的同学，请注意仔细阅读项目说明，你的IDE需要引入lombok插件！！！！
        val configs: List<WxMaProperties.MaConfig> = properties.configs
                ?: throw RuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！")
//        println("wx config:")
//        println(configs.joinToString(","){ it.toString() })
        val service: WxMaService = WxMaServiceImpl()
        service.wxMaConfig = properties.configs!![0].let { a ->
            val configStorage = WxMaDefaultConfigImpl()
            configStorage.appid = a.appId
            configStorage.secret = a.secret
            configStorage.token = a.token
            configStorage.aesKey = a.aesKey
            configStorage
        }
/*
        service.setMultiConfigStorages(configs
                .stream().map { a: WxMaProperties.MaConfig ->
                    val configStorage = WxMaDefaultConfigImpl()
                    configStorage.appid = a.appid
                    configStorage.secret = a.secret
                    configStorage.token = a.token
                    configStorage.aesKey = a.aesKey
                    configStorage
                }.collect(Collectors.toMap({ obj: WxMaDefaultConfigImpl -> obj.appid }, { a: WxMaDefaultConfigImpl? -> a }) { o: WxMaConfigStorage?, _: WxMaConfigStorage? -> o }))
*/
        return service
    }

    @Bean
    fun messageRouter(wxMpService: WxMpService?): WxMpMessageRouter {
        val newRouter = WxMpMessageRouter(wxMpService)
        return newRouter
    }
}

/**
 * wechat miniapp properties
 */
@ConfigurationProperties("wx.ma")
@Introspected
class WxMaProperties {

    var configs: List<MaConfig>? = null

    @Introspected
    class MaConfig {
        /**
         * 设置微信小程序的appid.
         */
        var appId: String? = null

        /**
         * 设置微信小程序的Secret.
         */
        var secret: String? = null

        /**
         * 设置微信小程序消息服务器配置的token.
         */
        var token: String? = null

        /**
         * 设置微信小程序消息服务器配置的EncodingAESKey.
         */
        var aesKey: String? = null

        /**
         * 消息格式，XML或者JSON.
         */
        var msgDataFormat: String? = null

        override fun toString(): String {
            return JsonHelper.toJson(this)
        }

    }

    override fun toString(): String {
        return JsonHelper.toJson(this)
    }
}