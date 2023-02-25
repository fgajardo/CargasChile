package com.example.cargaschile

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Otros {
    val id = 0

    fun int2shipmentType(i: Int): ShipmentType? {
        if (0 == i) return ShipmentType.Pendiente else if (1 == i) return ShipmentType.Asignado else if (2 == i) return ShipmentType.Terminado
        return ShipmentType.Indefinido
    }

    fun since(latest: Long): String? {
        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val strDate = fmt.format(latest)
        //Log.d("snc",strDate);
        val dNow = Date()
        val now = dNow.time
        val ti = Math.abs(now - latest) / 1000
        var rtn = ""
        if (ti < 60) rtn = String.format(
            "%d %s",
            ti,
            if (1L == ti) "segundo" else "segundos"
        ) else if (ti < 3600) rtn = String.format(
            "%d %s",
            ti / 60,
            if (1L == ti / 60) "minuto" else "minutos"
        ) else if (ti < 24 * 3600) rtn = String.format(
            "%d %s",
            ti / 3600,
            if (1L == ti / 3600) "hora" else "horas"
        ) else if (ti < 30 * 24 * 3600) rtn = String.format(
            "%d %s",
            ti / (24 * 3600),
            if (1L == ti / (24 * 3600)) "día" else "días"
        ) else if (ti < 12 * 30 * 24 * 3600) rtn = String.format(
            "%d %s",
            ti / (30 * 24 * 3600),
            if (1L == ti / (30 * 24 * 3600)) "mes" else "meses"
        ) else if (ti > 12 * 30 * 24 * 3600) rtn = String.format(
            "%d %s",
            ti / (12 * 30 * 24 * 3600),
            if (1L == ti / (12 * 30 * 24 * 3600)) "año" else "años"
        )
        return rtn
    }

    fun s2i(s: String?): Int {
        return if (null == s || 0 == s.length || s == "null") 0 else s.toInt()
    }

    fun singular(latest: Long): Boolean {
        val dNow = Date()
        val now = dNow.time
        val ti = Math.abs(now - latest) / 1000
        return if (1L == ti || 1L == ti / 60 || 1L == ti / 3600 || 1L == ti / (24 * 3600) || 1L == ti / (30 * 24 * 3600) || 1L == ti / (12 * 30 * 24 * 3600)) true else false
    }

    fun formatDate(d: Long): String? {
        val df1 = SimpleDateFormat("d")
        //SimpleDateFormat df2 = new SimpleDateFormat("MMMM");
        var df2 = ""
        val cal = Calendar.getInstance()
        cal.timeInMillis = d
        val month = cal[Calendar.MONTH]
        if (0 == month) df2 = "Enero" else if (1 == month) df2 =
            "Febrero" else if (2 == month) df2 = "Marzo" else if (3 == month) df2 =
            "Abril" else if (4 == month) df2 = "Mayo" else if (5 == month) df2 =
            "Junio" else if (6 == month) df2 = "Julio" else if (7 == month) df2 =
            "Agosto" else if (8 == month) df2 = "Septiembre" else if (9 == month) df2 =
            "Octubre" else if (10 == month) df2 = "Noviembre" else if (11 == month) df2 =
            "Diciembre"
        val df3 = SimpleDateFormat("HH:mm")
        return String.format("%s de %s a las %s", df1.format(d), df2, df3.format(d))
    }

    fun formatAmount(qtty: Int): String? {
        return NumberFormat.getIntegerInstance().format(qtty.toLong())
    }

    fun convert(input: String): String? {
        return input.replace("CRLF".toRegex(), System.getProperty("line.separator"))
    }

}