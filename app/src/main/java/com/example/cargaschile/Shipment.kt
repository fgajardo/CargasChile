package com.example.cargaschile

import android.graphics.Bitmap
import java.util.*

enum class Stage { posteado, postulado, asignado, recogido, transito, entregado, pageval, terminado }
enum class ShipmentType { Pendiente, Asignado, Terminado, Indefinido }

class Shipment {
    var id = -1
    var title = " "
    var status = -1
    var load = 0
    var maxPayment = 0
    var comments = " "
    var origin = " "
    var destination = " "
    lateinit var startingDate: Date
    fun int2stage(i: Int) : Stage {
        var s = Stage.posteado
        if(0 == i) s = Stage.posteado
        else if(1 == i) s = Stage.postulado
        else if(2 == i) s = Stage.asignado
        else if(3 == i) s = Stage.recogido
        else if(4 == i) s = Stage.transito
        else if(5 == i) s = Stage.entregado
        else if(6 == i) s = Stage.pageval
        else if(7 == i) s = Stage.terminado
        return s
    }

    fun getSubtitle() : String { return "subtitulo de $title" }
    fun desc(isDriver: Boolean, stage: Stage) : String {
        var rtn = ""
        if(isDriver) {
            if(Stage.posteado == stage) {
                rtn = "posteado"
            } else if(Stage.postulado == stage) {
                rtn = "postulado"
            } else if(Stage.asignado == stage) {
                rtn = "asignado"
            } else if(Stage.recogido == stage) {
                rtn = "recogido"
            } else if(Stage.transito == stage) {
                rtn = "transito"
            } else if(Stage.entregado == stage) {
                rtn = "entregado"
            } else if(Stage.pageval == stage) {
                rtn = "pageval"
            } else if(Stage.terminado == stage) {
                rtn = "terminado"
            }
            rtn += ", chofer"
        } else { // cliente
            if(Stage.posteado == stage) {
                rtn = "posteado" // editar envio si no hay postulantes, listado de postulantes en otro caso
            } else if(Stage.postulado == stage) {
                rtn = "postulado"
            } else if(Stage.asignado == stage) {
                rtn = "asignado"
            } else if(Stage.recogido == stage) {
                rtn = "recogido"
            } else if(Stage.transito == stage) {
                rtn = "transito"
            } else if(Stage.entregado == stage) {
                rtn = "entregado"
            } else if(Stage.pageval == stage) {
                rtn = "pageval"
            } else if(Stage.terminado == stage) {
                rtn = "terminado"
            }
            rtn += ", cliente"
        }
        return rtn
    }
}