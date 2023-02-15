package com.example.cargaschile

import android.graphics.Bitmap

enum class ShipmentType {
    Pendiente, Asignado, Terminado, Indefinido
}

class Shipment {
    var id = -1
    var status = -1
    var title = "yo"
    var details = "nada"
    fun getSubtitle() : String {
        return details
    }
    fun hasApe() : Boolean = false
}