package cn.hillwind.wx.demo.domain

class View {
    open class Public
    open class Internal : Public()
    class Admin : Internal()
}