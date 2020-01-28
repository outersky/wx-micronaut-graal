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

```
    ./gradlew run
```

也可以用：

```
./gradlew run --continuous
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

## 构建 native image

首先一定要用graal jvm，然后可以执行

```
    ./build-native.sh
```
