package com.example.cargaschile

import android.content.Context

object Model {
    lateinit var allComunas : HashMap<String, Comuna>
    lateinit var currentUser: User
    lateinit var currentShipment: Shipment
    var website = "UNDEFINED"
    var isDriver = false
    fun setSite(url: String) { website = url }
    fun setUser(map: Map<String, String>) {
        var login = map["login"].toString()
        var isDriver = false
        if("true" == map["isDriver"]) isDriver = true
        var carencia = 30
        if(null != map["carencia"]) carencia = map["carencia"]?.toInt()!!
        currentUser = User(login, isDriver, carencia)
    }
    fun setUser(name:String, isDrv: Boolean, keep: Int) {
        User(name, isDrv, keep).also { currentUser = it }
    }
    fun getUser() : User { return currentUser }
    fun getDataTable() : Tabla? {
        val t = Tabla()
        t.S1V0()
        println("Loaded "+t.dump())
        return t }

    fun getURL(path: String) : String { return "http//$path" }
    fun loadData(context: Context, rawWasLoaded: (res: ArrayList<Map<String, String>>, result: Int) -> Unit, url: String, args: String) {
        println("loadData: rul = $url, args = $args")
        var rtn = ArrayList<Map<String, String>>()
        if(args.equals("op_comunas=op_comunas")) {
            rawWasLoaded(rtn,0)
        }
    }

    fun loadFirstDataTabla(context: Context, rawWasLoaded: (res: ArrayList<Map<String, String>>, result: Int) -> Unit, url: String, args: String) {
        println("loadData: rul = $url, args = $args")
    }

    fun checkPresent(context: Context, rtn: String, login: String, email: String, driver:Boolean, telNum: Int, cb: (res: String) -> Unit) {
        cb(rtn)
    }

    fun sendCode() : String = "1234"

    fun dump() {}

}