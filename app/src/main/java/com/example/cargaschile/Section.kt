package com.example.cargaschile

class Section(items: ArrayList<Shipment>) {
    var sectionName: String
    var sectionType: ShipmentType
    var sectionItems: ArrayList<Shipment>

    init {
        val first = items[0]
        sectionType = int2type(first.status)
        sectionName = nameOf(sectionType)
        sectionItems = ArrayList<Shipment>()
        for (s in items) sectionItems.add(s)
    }

    fun numShipments() : Int {
        return sectionItems.size
    }

    fun removeAt(pos: Int) : Boolean {
        if((pos < 0)||(pos > numShipments()-1)) return false
        sectionItems.removeAt(pos)
        return true
    }
    fun nameOf(t: ShipmentType?): String {
        if (ShipmentType.Pendiente === t) return "PENDIENTES"
        else if (ShipmentType.Asignado === t) return "ASIGNADOS"
        else if (ShipmentType.Terminado === t) return "TERMINADOS"
        return "UNDEFINED"
    }

    private fun int2type(i: Int): ShipmentType {
        var it = ShipmentType.Indefinido
        if (0 == i) it = ShipmentType.Pendiente
        else if (1 == i) it = ShipmentType.Asignado
        else if (2 == i) it = ShipmentType.Terminado
        return it
    }

    fun dump() : String {
        var rtn = ""
        rtn = String.format("name='%s', type=%s, ",sectionName,nameOf(sectionType))
        if(null == sectionItems) rtn = String.format("%s NULL items",rtn)
        else rtn = String.format("%s %d items",rtn, sectionItems!!.size)
        return rtn
    }
}
