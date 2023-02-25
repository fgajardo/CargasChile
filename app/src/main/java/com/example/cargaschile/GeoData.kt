package com.example.cargaschile

object GeoData {

    private val comunasCachapoal = listOf(
        ComunaModel("Rancagua",1),
        ComunaModel("Codegua",2),
        ComunaModel("Coinco",3),
        ComunaModel("Coltauco",4),
        ComunaModel("Doñihue",5),
        ComunaModel("Graneros",6),
        ComunaModel("Las Cabras",7),
        ComunaModel("Machalí",8),
        ComunaModel("Malloa",9),
        ComunaModel("Mostazal",10),
        ComunaModel("Olivar",11),
        ComunaModel("Peumo",12),
        ComunaModel("Pichidegua",13),
        ComunaModel("Quinta de Tilcoco",14),
        ComunaModel("Rengo",15),
        ComunaModel("Requínoa",16),
        ComunaModel("San Vicente", 17)
    )

    private val comunasCardenalCaro = listOf(
        ComunaModel("Pichilemu",18),
        ComunaModel("La Estrella",19),
        ComunaModel("Litueche",20),
        ComunaModel("Marchihue",21),
        ComunaModel("Navidad",22),
        ComunaModel("Paredones",23)
    )

    private val comunasColchagua = listOf(
        ComunaModel("San Fernando",24),
        ComunaModel("Chépica",25),
        ComunaModel("Chimbarongo",26),
        ComunaModel("Lolol",27),
        ComunaModel("Nancagua",28),
        ComunaModel("Palmilla",29),
        ComunaModel("Peralillo",30),
        ComunaModel("Placilla",31),
        ComunaModel("Pumanque",32),
        ComunaModel("Santa Cruz",33)
    )

    val provinciasOHiggins = listOf(
        ProvinciaModel("Cachapoal", comunasCachapoal),
        ProvinciaModel("Cardenal Caro", comunasCardenalCaro),
        ProvinciaModel("Colchagua", comunasColchagua)
    )

    private val comunasCurico = listOf(
        ComunaModel("Curicó",34),
        ComunaModel("Hualañé",35),
        ComunaModel("Licantén",36),
        ComunaModel("Molina",37),
        ComunaModel("Rauco",38),
        ComunaModel("Romeral",39),
        ComunaModel("Sagrada Familia",40),
        ComunaModel("Teno",41),
        ComunaModel("Vichuquén",42)
    )

    private val comunasTalca = listOf(
        ComunaModel("Talca",43),
        ComunaModel("Constitución",44),
        ComunaModel("Curepto",45),
        ComunaModel("Empedrado",46),
        ComunaModel("Maule",47),
        ComunaModel("Pelarco",48),
        ComunaModel("Pencahue",49),
        ComunaModel("Río Claro",50),
        ComunaModel("San Clemente",51),
        ComunaModel("San Rafael",52)
    )

    private val comunasLinares = listOf(
        ComunaModel("Linares",53),
        ComunaModel("Colbún",54),
        ComunaModel("Longaví",55),
        ComunaModel("Parral",56),
        ComunaModel("Retiro",57),
        ComunaModel("San Javier",58),
        ComunaModel("Villa Alegre",59),
        ComunaModel("Yerbas Buenas",60)
    )

    private val comunasCauquenes = listOf(
        ComunaModel("Cauquenes",61),
        ComunaModel("Chanco",62),
        ComunaModel("Pelluhue",63)
    )

    val provinciasMaule = listOf(
        ProvinciaModel("Curicó", comunasCurico),
        ProvinciaModel("Talca", comunasTalca),
        ProvinciaModel("Linares", comunasLinares),
        ProvinciaModel("Cauquenes", comunasCauquenes)
    )

    private val comunasDiguillin = listOf(
        ComunaModel("Chillán",64),
        ComunaModel("Bulnes",65),
        ComunaModel("Chillán Viejo",66),
        ComunaModel("El Carmen",67),
        ComunaModel("Pemuco",68),
        ComunaModel("Pinto",69),
        ComunaModel("Quillón",70),
        ComunaModel("San Ignacio",71),
        ComunaModel("Yungay",72)
    )

    private val comunasItata = listOf(
        ComunaModel("Quirihue",73),
        ComunaModel("Cobquecura",74),
        ComunaModel("Coelemu",75),
        ComunaModel("Ninhue",76),
        ComunaModel("Portezuelo",77),
        ComunaModel("Ránquil",78),
        ComunaModel("Treguaco",79)
    )

    private val comunasPunilla = listOf(
        ComunaModel("San Carlos",80),
        ComunaModel("Coihueco",81),
        ComunaModel("Ñiquén",82),
        ComunaModel("San Fabián",83),
        ComunaModel("San Nicolás",84)
    )

    val provinciasNuble = listOf(
        ProvinciaModel("Diguillín", comunasDiguillin),
        ProvinciaModel("Itata", comunasItata),
        ProvinciaModel("Punilla", comunasPunilla)
    )

    val regionesZCS = listOf(
        RegionModel("O'Higgins", provinciasOHiggins),
        RegionModel("Maule", provinciasMaule),
        RegionModel("Ñuble", provinciasNuble)
    )

}