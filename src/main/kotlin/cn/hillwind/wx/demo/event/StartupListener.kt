package cn.hillwind.wx.demo.event

import cn.binarywang.wx.miniapp.api.WxMaService
import cn.hillwind.wx.cloud.WxCloudDbQuery
import cn.hillwind.wx.cloud.WxCloudService
import cn.hillwind.wx.demo.util.debug
import cn.hillwind.wx.demo.util.toDate
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.binarywang.wxpay.bean.request.WxPayDownloadBillRequest
import com.github.binarywang.wxpay.service.WxPayService
import com.github.binarywang.wxpay.util.XmlConfig
import io.micronaut.context.ApplicationContext
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import me.chanjar.weixin.mp.api.WxMpService
import java.io.File
import java.time.LocalDate
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
    lateinit var wxMaService: WxMaService

    @Inject
    lateinit var wxPayService: WxPayService

    @Inject
    lateinit var wxCloudService: WxCloudService

    fun testWx() {
        /*
        //获取用户信息
        "Get UserInfo: ".debug()
        val userInfo = wxMpService.userService.userInfo("od-im5MNGQTdvvJWDqhPM2aarDJY")
        userInfo.toString().debug()
        */
        //("Weixin AccessToken: " + wxMpService.accessToken).debug()

        //获取accessToken
        "Get Miniapp QrCode: ".debug()
        wxMaService.accessToken.debug()

        //获取小程序周访问趋势
        val begin = LocalDate.of(2020, 1, 6)
        val end = LocalDate.of(2020, 1, 12)
        begin.toString().debug()
        end.toString().debug()
        wxMaService.analysisService.getWeeklyVisitTrend(begin.toDate(), end.toDate()).forEach {
            it.visitPv.debug()
        }

        //获取小程序二维码
        "Get Miniapp QrCode: ".debug()
        val file = File("/tmp/wxa.jpg")
        if (file.exists()) file.delete()
        wxMaService.qrcodeService.createWxaCode("pages/home/home").copyTo(file)

        //下载订单
        "Download bill 20200104: ".debug()
        val billResult = wxPayService.downloadBill(WxPayDownloadBillRequest.newBuilder().billDate("20200104").billType("ALL").build())
        ("TotalFee: " + billResult.totalFee).debug()

        //查询云数据库中bills集合的数量
        "Now Query Cloud: ".debug()
        val query = "db.collection('bills').count()"
        val result = wxCloudService.dbService().count(WxCloudDbQuery(query))
        result.toJson().debug()
    }

}