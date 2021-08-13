package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.finalproject.databinding.ActivityLoadingBinding
import data.Book
import data.Weather
import data.OpenWeather_fm

class LoadingActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoadingBinding
    //repository객체 만들기
    private val weatherRepository = WeatherRepository()
    private val FMrepositary = FMrepositary()
    private val BookRepository = BookRepository()
    private var location:String =""
    private var weather_state:String =""
    private var current_temp:Double =0.0
    private var max_temp:Double =0.0
    private var min_temp:Double =0.0
    private var feels_like:Double =0.0
    private var wind_speed:Double=0.0
    private var humadity:Int=0
    private var rainValue:Double=0.0
    private var pm10value:Double=0.0
    private var title = arrayListOf<String>()
    private var coverSmallUrl = arrayListOf<String>()
    private var description= arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //로딩 아이콘 회전 애니메이션
        runOnUiThread {
            var rotate = AnimationUtils.loadAnimation(applicationContext, R.anim.rotating_loading)
            binding.loadingIcon.startAnimation(rotate)
        }
        weatherRepository.loadWeatherNotice(this) //정상 처리되면, MainActivity 전환
        FMrepositary.loadFMNotice(this)

        startLoading()
    }
    //데이터 가져오기
    fun loadComplete(data: Weather){
        location = data.name //도시 이름 저장
        weather_state = data.weather[0].weather_desc
        current_temp = data.main.temp
        max_temp = data.main.temp_max
        min_temp = data.main.temp_min
        feels_like = data.main.feels_like
        wind_speed = data.wind.speed
        if(data.rain!=null) {
            rainValue = data.rain.rain1h
        }
        humadity = data.main.humidity
        if(weather_state=="light rain"||weather_state=="heavy rain"||weather_state=="moderate rain"||weather_state=="heavy intensity rain"||weather_state=="thunderstorm with light rain"){
            query = "비 내리는"
        }
        else if(weather_state == "few clouds" ||weather_state=="mist"||weather_state=="broken clouds"||weather_state=="haze"||weather_state == "scattered clouds"||weather_state=="overcast clouds"){
            query = "구름"
        }
        else if(weather_state=="clear sky"){
            query ="나들이"
        }
        BookRepository.loadBookNotice(this)
    }
    fun loadComplete2(data: OpenWeather_fm){
        pm10value = data.list[0].components.pm10
    }
    fun loadComplete3(data: Book) {
        title.clear()
        coverSmallUrl.clear()
        description.clear()
        for (i in data.item.indices) {
            title.add(data.item[i].title)
            coverSmallUrl.add(data.item[i].coverLargeUrl)
            description.add(data.item[i].description)
        }

    }
    //데이터 보내기
    private fun startLoading() { val handler = Handler()
        handler.postDelayed(Runnable {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("location",location)
            intent.putExtra("weather_state",weather_state)
            intent.putExtra("current_temp",current_temp)
            intent.putExtra("max_temp",max_temp)
            intent.putExtra("min_temp",min_temp)
            intent.putExtra("feels_like",feels_like)
            intent.putExtra("wind_speed",wind_speed)
            intent.putExtra("humadity",humadity)
            intent.putExtra("rain1h",rainValue)
            intent.putExtra("pm10value",pm10value)
            intent.putExtra("title",title)
            intent.putExtra("coverSmallUrl",coverSmallUrl)
            intent.putExtra("description",description)
            startActivity(intent)
            finish()
            }
            , 2000)
    }

}
