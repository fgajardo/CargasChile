package com.example.cargaschile

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object Model {
    lateinit var data : Tabla
    lateinit var allComunas : HashMap<String, Comuna>
    lateinit var currentUser: User
    lateinit var currentShipment: Shipment
    private lateinit var callback : (res: ArrayList<HashMap<String,String>>, result: Int) -> Unit
    private lateinit var mParams : HashMap<String, String>
    var website = "UNDEFINED"
    var isDriver = false

    fun setCallback(cb : (res: ArrayList<HashMap<String,String>>, result: Int) -> Unit) {
        callback = cb
    }

    fun parse(args: String) : HashMap<String, String> {
        var params: HashMap<String, String> = HashMap<String, String>()
        if(args.isEmpty()) return params
        var pairs = args.split("&")
        for(i in pairs.indices) {
            var curr = pairs[i].split("=")
            val key = curr[0]
            var value = ""
            if(curr.size > 1) value = curr[1]
            params[key] = value
        }
        return params
    }

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
        currentUser = User(name, isDrv, keep)
        data = Tabla()
    }
    fun getUser() : User { return currentUser }
    fun getDataTable() : Tabla? { return data }

    fun getURL(path: String) : String { return "http//$path" }    fun whatIs(str: String) : Int {
        if(str.isEmpty()) return 0
        var rtn = 0
        try {
            JSONArray(str)
            rtn = 1
        } catch(e: JSONException) {
        }
        try {
            JSONObject(str)
            rtn = 2
        } catch(e: JSONException) {
        }
        return rtn
    }

    fun isJSONArray(s: String) : Boolean { return 1 == whatIs(s) }

    fun post(ctx: Context, url: String?, args: String?) {
        mParams = parse(args!!)
        val list = java.util.ArrayList<HashMap<String, String>>()
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                if(isJSONArray(response)) {
                    val ja = JSONArray(response)
                    for (i in 0 until ja.length()) {
                        val jo = ja.getJSONObject(i)
                        val map = HashMap<String, String>()
                        val iterator: Iterator<*> = jo.keys()
                        while (iterator.hasNext()) {
                            val key = iterator.next() as String
                            val value = jo.getString(key)
                            map[key] = value
                        } // while
                        list.add(map)
                    } // for
                    callback(list, 0)
                } else callback(list, 2)
            }, // onResponse
            // listener
            Response.ErrorListener { error -> callback(list, 1) } // onErrorResponse
        ) {
            // )stringRequest
            override fun getParams(): Map<String, String>? {
                return mParams
            }
        } // last arg
        VolleySingleton.getInstance(ctx).addToRequestQueue(stringRequest)
    } // post



    fun loadData(context: Context, rawWasLoaded: (res: ArrayList<Map<String, String>>, result: Int) -> Unit, url: String, args: String) {
        println("loadData: rul = $url, args = $args")
        var rtn = ArrayList<Map<String, String>>()
        if(args.equals("op_comunas=op_comunas")) {
            rawWasLoaded(rtn,0)
        }
        else if(args.contains("op_login")) {
            var map = HashMap<String, String>()
            map["login"] = "luchito"
            map["isDriver"] = "1"
            map["carencia"] = "32"
            map["site"] = "http://novacea.cl/ws/"
            rtn.add(map)
            rtn.add(map)
            rawWasLoaded(rtn,0)
        }
        else if(args.contains("bidsForShipment")) {
            val caller = url[7].toInt()
            println("Caller is "+caller)
        }
    }

    fun loadFirstDataTabla(context: Context, wasLoaded: (res: Int) -> Unit, url: String, args: String) {
        println("loadFirstDataTabla: url = $url, args = $args")
        data = Tabla()
        data.Schofer()
        wasLoaded(0)
    }

    fun checkPresent(context: Context, rtn: String, login: String, email: String, driver:Boolean, telNum: Int, cb: (res: String) -> Unit) {
        // check if login, email & eventually phone are already present
        // post(url, args) -> callback
        val usrOk = true
        val emailOk = true
        var phoneOk = true
        if(driver) phoneOk = false
        var wrong = 0
        if(!usrOk) wrong++
        if(!emailOk) wrong++
        if(!phoneOk) wrong++
        val res = ArrayList<HashMap<String, String>>()
        val map = HashMap<String,String>()
        if(0 == wrong) {
            // get code, sendmail
            map["code"] = "1234"
        } else {
            if(1 == wrong) {
                var sub = ""
                if(!usrOk) sub = "nombre de usuario"
                else if(!emailOk) sub = "correo electrónico"
                else if(!phoneOk) sub = "teléfono"
                map["present"] = "Ese $sub ya está registrado"
            } else if(2 == wrong) {
                var sub = ""
                var sub2 = ""
                if(!usrOk) sub = "nombre de usuario"
                if(!emailOk) {
                    if(sub.isEmpty()) sub = "correo electrónico"
                    else sub2 = "correo electrónico"
                }
                if(!phoneOk) {
                    if(sub.isEmpty()) sub = "teléfono"
                    else sub2 = "teléfono"
                }
                map["present"] = "Ese $sub y $sub2 ya están registrados"
            } else if(3 == wrong) map["present"] = "Ese nombre de usuario, correo electrónico y teléfono ya están registrados"
        }
        res.add(map)
        callback(res, 0)
        cb(rtn)
    }

    fun sendCode() : String = "1234"

    fun dump() {}

}