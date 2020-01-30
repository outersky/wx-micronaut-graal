package cn.hillwind.wx.demo.conf

import cn.hillwind.wx.cloud.WxCloudDbService
import cn.hillwind.wx.cloud.WxCloudProperties
import cn.hillwind.wx.cloud.WxCloudService
import cn.hillwind.wx.cloud.WxCloudStorageService
import cn.hillwind.wx.cloud.impl.WxCloudServiceDefaultImpl
import cn.hillwind.wx.cloud.impl.WxCloudServiceImpl
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Factory
import io.micronaut.core.annotation.Introspected
import me.chanjar.weixin.mp.api.WxMpService
import javax.inject.Inject

/**
 * 云开发的有关配置
 */
@Factory
class WxCloudConfiguration @Inject constructor(private val wxMpService: WxMpService, private val cloudProperties: WxCloudProperties) {

    @Bean
    fun wxCloudService(): WxCloudService {
        return WxCloudServiceImpl(wxMpService, cloudProperties)
    }

    @Bean
    fun wxCloudDbService(): WxCloudDbService {
        return WxCloudServiceDefaultImpl(wxMpService, cloudProperties)
    }

    @Bean
    fun wxCloudStorageService(): WxCloudStorageService {
        return WxCloudServiceDefaultImpl(wxMpService, cloudProperties)
    }

}

/**
 * wechat cloud properties
 *
 */
@ConfigurationProperties("wx.cloud")
@Introspected
class WxCloudSpringProperties : WxCloudProperties()