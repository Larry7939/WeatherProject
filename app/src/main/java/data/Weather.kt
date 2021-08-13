package data
import com.google.gson.annotations.SerializedName
data class Weather(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherX>,
    val wind: Wind,
    val rain:Rain
)
data class WeatherX(
    //마음에 안드는 변수명 변경 description -> weather_desc
    @SerializedName("description")
    val weather_desc: String,
    @SerializedName("icon")
    val weather_icon: String,
    val id: Int,
    @SerializedName("main")
    val weather_state: String
)
data class Wind(
    val deg: Int,
    val speed: Double
)
data class Rain(
    @SerializedName("1h")
    val rain1h: Double,
    @SerializedName("3h")
    val rain3h: Double
)
data class Clouds(
    val all: Int
)
data class Coord(
    val lat: Double,
    val lon: Double
)
data class Main(
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double,
    val aqi: Int
)
data class Sys(
    val country: String,
    val id: Int,
    val message: Double,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)