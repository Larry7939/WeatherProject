package data
data class OpenWeather_fm(
    val coord: Coord1,
    val list: List<Fm>
)
data class Fm(
    val main: Main ,
    val components: Components,
    val dt: Int
)
data class Components(
    val co: Double,
    val nh3: Double,
    val no: Double,
    val no2: Double,
    val o3: Double,
    val pm10: Double,
    val pm2_5: Double,
    val so2: Double
)
//data class Main1(
//    val aqi: Int
//)

data class Coord1(
    val lat: Double,
    val lon: Double
)
