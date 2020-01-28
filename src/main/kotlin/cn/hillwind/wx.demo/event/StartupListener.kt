package cn.hillwind.wx.demo.event

import cn.hillwind.wx.demo.util.debug
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.binarywang.wxpay.bean.request.WxPayDownloadBillRequest
import com.github.binarywang.wxpay.service.WxPayService
import com.github.binarywang.wxpay.util.XmlConfig
import io.micronaut.context.ApplicationContext
import io.micronaut.context.event.StartupEvent
import io.micronaut.http.server.netty.jackson.JsonViewMediaTypeCodecFactory
import io.micronaut.http.server.netty.jackson.JsonViewServerFilter
import io.micronaut.runtime.event.annotation.EventListener
import me.chanjar.weixin.mp.api.WxMpService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartupListener {

    @Inject
    lateinit var mapper: ObjectMapper

    @Inject
    lateinit var context: ApplicationContext

    @EventListener
    fun onStartupEvent(event: StartupEvent) {
        //启用微信支付的快速模式，避免用反射，否则不能构建 native-image
        println("Demo startup ")
        XmlConfig.fastMode = true
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
//        println(context.containsBean(JsonViewServerFilter::class.java))
//        println(context.containsBean(JsonViewMediaTypeCodecFactory::class.java))

        testWx()

/*
        val user = User.findByLoginName("admin")!!  // userService.initTestData()
        try {
            val result = mapper
                    .writerWithView(Public::class.java)
                    .writeValueAsString(user.roles.first())
            println("JsonView: $result")
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
*/

    }

    @Inject
    lateinit var wxMpService: WxMpService

    @Inject
    lateinit var wxPayService: WxPayService

    fun testWx() {
        ("Weixin AccessToken: " + wxMpService.accessToken).debug()
        "Download bill 20200104: ".debug()
        val billResult = wxPayService.downloadBill(WxPayDownloadBillRequest.newBuilder().billDate("20200104").billType("ALL").build())
        ("TotalFee: " + billResult.totalFee).debug()
    }
}