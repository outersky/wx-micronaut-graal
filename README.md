# wx-micronaut-graal
微信服务端 + micronaut + graal demo

本项目主要测试微信有关的微服务开发，最终需要构建一个native image，以脱离JVM执行服务。

## 开发

- 采用 Kotlin 开发
- 尽量减少反射机制的使用，会导致无法构建native image
- 使用micronaut框架，因为spring里面使用了太多的反射等机制
- 使用mariadb数据库驱动，mysql的驱动里面使用了太多反射

## 运行

需要设置 src/main/resources/application.yml中有关微信、数据库等参数，设置方法参考 [WxJava](https://github.com/Wechat-Group/WxJava)

测试环境：
- 操作系统： Linux Mint 19.3 Tricia
- Java开发环境：Java HotSpot(TM) 64-Bit Server VM (build 25.191-b12, mixed mode)
- GraalVM版本：OpenJDK 64-Bit GraalVM CE 19.3.1 (build 25.242-b06-jvmci-19.3-b07, mixed mode)
- 数据库： 10.1.43-MariaDB-0ubuntu0.18.04.1 Ubuntu 18.04
- CPU: Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz
- RAM: 16G

运行命令：
```
> ./gradlew run
```

也可以用：

```
> ./gradlew run --continuous
```

这样每次修改了src/main/kotlin下面的代码，都会自动重启服务。
因为在application.yml 文件中设置了：

```
micronaut:
  io:
    watch:
      paths: src/main/kotlin
      restart: true
```

这样micronaut就会监控src/main/kotlin目录，发现变化了就重启.

在我本机启动以后是这样的：

```
> ./gradlew run

> Task :run
17:08:38.954 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
17:08:39.001 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed.
17:08:39.030 [main] WARN  io.micronaut.data.query - CREATE Statement Failed: (conn=167) Table 'role' already exists
17:08:39.030 [main] WARN  io.micronaut.data.query - CREATE Statement Failed: (conn=167) Table 'manufacturer' already exists
Demo startup 
Weixin AccessToken: 29_FXT0zWObnkQCuAvOBX21wMuukYbb2sk20OYk82ByB1ACpYI5g3VmPvX3wFn3isCfUlXr68iF1ulsOYeGxoDvt0BTv1048CMbR43wYEPOp-WS8ti-Ik-YDkp8lBQzuIA7DHdg_lElXvIQSNuxZIXcAEASBQ
Download bill 20200104: 
17:08:40.192 [main] INFO  c.g.b.w.s.impl.WxPayServiceImpl - 
【请求地址】：https://api.mch.weixin.qq.com/pay/downloadbill
【请求数据】：<?xml version="1.0" encoding="UTF-8"?><xml>
    <nonce_str>1580202520002</nonce_str>
    <bill_date>20200104</bill_date>
    <appid>wx0e25d797adcdc399</appid>
    <sign>5F75E5E36A37478C4FC4767782489B7E</sign>
    <bill_type>ALL</bill_type>
    <mch_id>1314217101</mch_id>
</xml>

【响应数据】：﻿交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,总金额,企业红包金额,微信退款单号,商户退款单号,退款金额,企业红包退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率
`2020-01-04 17:43:52,`wx0e25d797adcdc399,`1314217101,`0,`,`4200000480202001043149128488,`aas,`od-im5MNGQTdvvJWDqhPM2aarDJY,`JSAPI,`SUCCESS,`BOSH_CREDIT,`CNY,`0.12,`0.00,`0,`0,`0.00,`0.00,`,`,`商品简单描述,`,`0.00000,`1.00%
`2020-01-04 16:45:31,`wx0e25d797adcdc399,`1314217101,`0,`,`4200000475202001040513553255,`111,`od-im5MNGQTdvvJWDqhPM2aarDJY,`JSAPI,`SUCCESS,`BOSH_CREDIT,`CNY,`0.01,`0.00,`0,`0,`0.00,`0.00,`,`,`商品简单描述,`,`0.00000,`1.00%
总交易单数,总交易额,总退款金额,总企业红包退款金额,手续费总金额
`2,`0.13,`0.00,`0.00,`0.00000

TotalFee: 0.13
17:08:40.408 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 2098ms. Server Running: http://localhost:8080

```

## 构建 native image

1：先构建fatJar：

```
> ./gradlew clean assemble
```

2：切换到graal jvm：

```
> java -version
openjdk version "1.8.0_242"
OpenJDK Runtime Environment (build 1.8.0_242-b06)
OpenJDK 64-Bit GraalVM CE 19.3.1 (build 25.242-b06-jvmci-19.3-b07, mixed mode)

```

3: 构建native-image

```
> ./build-native.sh
```

你会看到一堆输出，然后就是缓慢的编译过程，在我的电脑上是这样的（前半段有很多类的信息，这里忽略）：
```
...
...
ServiceLoaderFeature: processing service class kotlin.reflect.jvm.internal.impl.builtins.BuiltInsLoader
  adding implementation class: kotlin.reflect.jvm.internal.impl.serialization.deserialization.builtins.BuiltInsLoaderImpl
ServiceLoaderFeature: processing service class kotlin.reflect.jvm.internal.impl.resolve.ExternalOverridabilityCondition
  adding implementation class: kotlin.reflect.jvm.internal.impl.load.java.ErasedOverridabilityCondition
  adding implementation class: kotlin.reflect.jvm.internal.impl.load.java.FieldOverridabilityCondition
  adding implementation class: kotlin.reflect.jvm.internal.impl.load.java.JavaIncompatibilityRulesOverridabilityCondition
[demo:14432]   (typeflow):  53,814.88 ms
[demo:14432]    (objects):  43,225.05 ms
[demo:14432]   (features):   5,282.66 ms
[demo:14432]     analysis: 108,964.84 ms
[demo:14432]     (clinit):   2,206.20 ms
[demo:14432]     universe:   4,703.26 ms
[demo:14432]      (parse):   8,963.47 ms
[demo:14432]     (inline):   7,953.23 ms
[demo:14432]    (compile):  57,048.50 ms
[demo:14432]      compile:  77,493.55 ms
[demo:14432]        image:   5,291.08 ms
[demo:14432]        write:   1,298.71 ms
[demo:14432]      [total]: 212,357.21 ms
```

最后一行的[total]数据显示：总共花了212秒才构建完成。

然后你就会看到目录下面多了一个demo的可执行文件：
```
> ls -lh
total 77M
...
-rwxrwxrwx 1 root root  77M Jan 28 17:15 demo
...
```

这个文件就可以copy了到处直接执行了：
```
> ./demo 
19:01:02.566 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
19:01:02.568 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed.
19:01:02.571 [main] WARN  io.micronaut.data.query - CREATE Statement Failed: (conn=327) Table 'role' already exists
19:01:02.571 [main] WARN  io.micronaut.data.query - CREATE Statement Failed: (conn=327) Table 'manufacturer' already exists
Demo startup 
Weixin AccessToken: 29_OmF2La14HettkM7JgWcpc4xQll72kO9OQnpyfdzUt2mzDqGb7Aqc-EQ8LzeNVA3MazRGQsndyJsHfkMrRjOcknHce293QXyY63yq-F4IaWJFRcmp6cjUNHICD6PPG282C6-V2ZIFx1KNtZxqPZOiAFAEOD
Download bill 20200104: 
19:01:02.896 [main] INFO  c.g.b.w.s.impl.WxPayServiceImpl - 
【请求地址】：https://api.mch.weixin.qq.com/pay/downloadbill
【请求数据】：<?xml version="1.0" encoding="UTF-8"?>
<xml><nonce_str>1580209262768</nonce_str><bill_date>20200104</bill_date><appid>wx0e25d797adcdc399</appid><sign>E35BCCBAC497FCE6EA27873CDAC18EC3</sign><bill_type>ALL</bill_type><mch_id>1314217101</mch_id></xml>
【响应数据】：﻿交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,总金额,企业红包金额,微信退款单号,商户退款单号,退款金额,企业红包退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率
`2020-01-04 17:43:52,`wx0e25d797adcdc399,`1314217101,`0,`,`4200000480202001043149128488,`aas,`od-im5MNGQTdvvJWDqhPM2aarDJY,`JSAPI,`SUCCESS,`BOSH_CREDIT,`CNY,`0.12,`0.00,`0,`0,`0.00,`0.00,`,`,`商品简单描述,`,`0.00000,`1.00%
`2020-01-04 16:45:31,`wx0e25d797adcdc399,`1314217101,`0,`,`4200000475202001040513553255,`111,`od-im5MNGQTdvvJWDqhPM2aarDJY,`JSAPI,`SUCCESS,`BOSH_CREDIT,`CNY,`0.01,`0.00,`0,`0,`0.00,`0.00,`,`,`商品简单描述,`,`0.00000,`1.00%
总交易单数,总交易额,总退款金额,总企业红包退款金额,手续费总金额
`2,`0.13,`0.00,`0.00,`0.00000

TotalFee: 0.13
19:01:02.929 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 387ms. Server Running: http://localhost:8080

```

可以看到启动时间明显缩短了，只有 387ms 。
而且通过top查看，只占用了20M内存。
