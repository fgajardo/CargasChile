package com.example.cargaschile

object Shared {
    lateinit var cb: (ret: Int) -> Unit
    fun comuna(id: Int): String {
        var rtn = "Nada"
        when (id) {
            1 -> rtn = "Rancagua"
            2 -> rtn = "Codegua"
            54 -> rtn = "Colbún"
        }
        return rtn
    }
    fun provincia(id: Int): String {
        var rtn = "Nada"
        when (id) {
            1 -> rtn = "Cachapoal"
            2 -> rtn = "Cachapoal"
            54 -> rtn = "Linares"
        }
        return rtn
    }
    fun region(id: Int): String {
        var rtn = "Nada"
        when (id) {
            1 -> rtn = "O'Higgins"
            2 -> rtn = "O'Higgins"
            54 -> rtn = "Maule"
        }
        return rtn
    }
}