package com.example.cargaschile

class Tabla {
    private var sections = ArrayList<Section>()

    fun Tabla() {
        sections = ArrayList()
    }

    fun S1V1() {
        val s = Section(ArrayList<Shipment>())
        if(null == s) println("NULL s")
        append(s)
    }

    fun S1V0() {
        val q = ArrayList<Shipment>()
        val s0 = Shipment()
        s0.id = 0
        s0.title = "Primero"
        s0.status = 0
        q.add(s0)
        val s1 = Shipment()
        s1.id = 1
        s1.title = "Segundo"
        s1.status = 0
        q.add(s1)
        val s = Section(q)
        println("Agregando seccion "+s.dump())
        append(s)
        println("Quedo asi:"+dump())
    }

    fun S2V2() {

    }

    fun S2V1() {

    }

    fun S2V0() {

    }
    fun append(newSection: Section?) {
        if(null == newSection) println("newSection es NULL")
        if(null == sections) println("section es NULL")
        else println("Sections partio con "+sections.size)
        if (newSection != null) {
            sections!!.add(newSection)
        }
        else println("Sections quedo con "+sections.size)
    }

    fun removeSectionAt(section: Int) {
        sections!!.removeAt(section)
    }

    fun numberOfSections(): Int {
        return sections!!.size
    }

    fun numberOfItemsAt(section: Int): Int {
        if(sections.isEmpty()) return 0
        val s = sections!![section]
        return s.sectionItems!!.size
    }

    fun nameOfSectionAt(section: Int): String? {
        if(sections.isEmpty()) return ""
        val s = sections!![section]
        return s.sectionName
    }

    fun typeOfSectionAt(section: Int): ShipmentType? {
        val s = sections!![section]
        return s.sectionType
    }

    fun itemAt(sec: Int, row: Int): Shipment? {
        val s = sections!![sec]
        return s.sectionItems!![row]
    }

    fun removeItemAt(sec: Int, row: Int) : Boolean {
        if((sec < 0)||(sec > sections.size-1)) return false
        var currSection = sections[sec]
        if((row < 0)||(row > currSection.numShipments() - 1)) return true
        currSection.removeAt(row)
        if(0 == currSection.numShipments())
            sections.removeAt(sec)
        return true
    }

    fun dump(): String? {
        val ns = sections.size
        if (0 == ns) {
            return "TABLA VACIA"
        }
        var rtn: String? = String.format("TABLA CON %d SECCIONES\n", ns)
        for (i in 0 until ns) {
            val s = sections[i]
            rtn = String.format("%sSECCIÓN %d, '%s'\n", rtn, i, s.sectionName)
            if(null == s.sectionItems) rtn = String.format("%ssectionItems es NULL",rtn)
            else {
                for (j in s.sectionItems!!.indices) {
                    val item = s.sectionItems!![j]
                    rtn = java.lang.String.format(
                        "%s[%d]: name = '%s', status = %d",
                        rtn,
                        j,
                        item.title,
                        item.status
                    )
                }
            }
        }
        return rtn
    }

}