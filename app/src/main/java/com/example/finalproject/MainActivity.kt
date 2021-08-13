package com.example.finalproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import com.example.finalproject.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import kotlin.math.roundToInt


data class Bookmain(val iv: Bitmap?, val intro:String,val desc:String)
class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    private var location:String =""
    private var weather_state:String =""
    private var current_temp:Double=0.0
    private var max_temp:Double =0.0
    private var min_temp:Double =0.0
    private var feels_like:Double =0.0
    private var wind_speed:Double=0.0
    private var humadity:Int=0
    private var rainValue:Double=0.0
    private var pm10value:Double=0.0
    private var is_opened=0
    private var unitOn=0
    private var booklist = ArrayList<Bookmain>()
    private var title = arrayListOf<String>()
    private var coverSmallUrl = arrayListOf<String>()
    private var description = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDataFromLoading()
        setWeatherInfo()
        setDrawer()
        setBookInfo()
        binding.swipeRefreshLayout.setOnRefreshListener {
            val intent = Intent(this,LoadingActivity::class.java)
            startActivity(intent)
        }
    }

    //기온단위 선택 저장용
    override fun onResume() {
        super.onResume()
        val sp = getSharedPreferences("unitOn", MODE_PRIVATE)
        unitOn = sp.getInt("unit",0)
    }
    override fun onPause(){
        super.onPause()
        val sp = getSharedPreferences("unitOn", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putInt("unit",unitOn)
        editor.commit()
    }
    private fun setDataFromLoading(){
        location = intent.getStringExtra("location").toString()
        weather_state = intent.getStringExtra("weather_state").toString()
        current_temp = intent.getDoubleExtra("current_temp",0.0)
        max_temp = intent.getDoubleExtra("max_temp",0.0)
        min_temp = intent.getDoubleExtra("min_temp",0.0)
        feels_like = intent.getDoubleExtra("feels_like",0.0)
        wind_speed = intent.getDoubleExtra("wind_speed",0.0)
        humadity = intent.getIntExtra("humadity",0)
        rainValue = intent.getDoubleExtra("rain1h",0.0)
        pm10value = intent.getDoubleExtra("pm10value",0.0)
    }
    //set된 변수를 MainActivity binding요소에 넣는 함수
    private fun setWeatherInfo(){
        when (location) {
            "Seoul" -> { binding.location.text = "서울 특별시" }
            "Incheon" -> { binding.location.text="인천 광역시" }
            "Daejeon" -> { binding.location.text="대전 광역시" }
            "Sejong" -> {binding.location.text="세종특별자치시"}
            "Gwangju" -> { binding.location.text ="광주 광역시" }
            "Daegu" -> { binding.location.text="대구 광역시" }
            "Busan" -> { binding.location.text="부산 광역시" }
            "Ulsan" -> { binding.location.text="울산 광역시" }
            "Jeju City" -> { binding.location.text="제주특별자치도 제주시" }
        }
        if(weather_state == "few clouds" ||weather_state=="mist"||weather_state=="broken clouds"||weather_state=="haze"||weather_state == "scattered clouds"){
            binding.weatherIcon.setImageResource(R.drawable.mist)
            binding.back1.setImageResource(R.drawable.clear_background1)
            binding.back2.setImageResource(R.drawable.fewcloud_background2)
            binding.swipeRefreshLayout.background= R.drawable.clear_background1.toDrawable()
            binding.weatherState.text="구름 많음"
        }
        else if(weather_state=="overcast clouds") {
            binding.weatherIcon.setImageResource(R.drawable.cloudy)
            binding.back1.setImageResource(R.drawable.cloudy_background1)
            binding.back2.setImageResource(R.drawable.cloudy_background2)
            binding.swipeRefreshLayout.background= R.drawable.cloudy_background1.toDrawable()

            binding.weatherState.text = "흐림"
        }
        else if(weather_state=="clear sky"){
            binding.weatherIcon.setImageResource(R.drawable.clear)
            binding.back1.setImageResource(R.drawable.clear_background1)
            binding.back2.setImageResource(R.drawable.clear_background2)
            binding.swipeRefreshLayout.background= R.drawable.clear_background1.toDrawable()
            binding.weatherState.text = "맑음"
            var rotate = AnimationUtils.loadAnimation(applicationContext,R.anim.rotating_sun)
            runOnUiThread{
                binding.weatherIcon.startAnimation(rotate)
            }
        }
        else if(weather_state=="light rain"||weather_state=="heavy rain"||weather_state=="moderate rain"||weather_state=="heavy intensity rain"||weather_state=="thunderstorm with light rain"){
            binding.weatherIcon.setImageResource(R.drawable.rain)
            binding.back1.setImageResource(R.drawable.rainy_background1)
            binding.back2.setImageResource(R.drawable.rainy_background2)
            binding.swipeRefreshLayout.background= R.drawable.rainy_background1.toDrawable()
            binding.weatherState.text = "비"
        }
        binding.currentTemp.text = (current_temp.toInt().toString()+"°")
        binding.maxTemp.text =max_temp.toInt().toString()+"°"
        binding.minTemp.text =min_temp.toInt().toString()+"°"
        binding.feelLike.text = "체감온도 "+feels_like.toInt().toString()+"°"
        if(wind_speed<=1.5) {
            binding.windValue.text = "고요함"
            var rotate1 = AnimationUtils.loadAnimation(applicationContext, R.anim.rotating_wind1)//강풍
            runOnUiThread {
                    binding.windBlade.startAnimation(rotate1)
            }
        }
        else if(wind_speed>1.5 && wind_speed<5.0){
            binding.windValue.text = "약함"
            var rotate2 = AnimationUtils.loadAnimation(applicationContext, R.anim.rotating_wind2)//강풍
            runOnUiThread {
                    binding.windBlade.startAnimation(rotate2)
            }
        }
        else if(wind_speed>=5.0){
            binding.windValue.text="강함"
            var rotate3 = AnimationUtils.loadAnimation(applicationContext, R.anim.rotating_wind3)//강풍
            runOnUiThread {
                    binding.windBlade.startAnimation(rotate3)
            }
        }
        binding.humadityValue.text = humadity.toString()+"%"
        if(humadity<10){
            binding.humadity.setImageResource(R.drawable.humadity_6)
        }
        else if(humadity in 11..20){
            binding.humadity.setImageResource(R.drawable.humadity_16)
        }
        else if(humadity in 21..30){
            binding.humadity.setImageResource(R.drawable.humadity_26)
        }
        else if(humadity in 31..55){
            binding.humadity.setImageResource(R.drawable.humadity_46)
        }
        else if(humadity in 56..75){
            binding.humadity.setImageResource(R.drawable.humadity_66)
        }
        else if(humadity in 76..90){
            binding.humadity.setImageResource(R.drawable.humadity_86)
        }
        else if(humadity in 91..100){
            binding.humadity.setImageResource(R.drawable.humadity_96)
        }
        if(pm10value in 0.0..15.99){
            binding.mustValue.text="매우 좋음"
            binding.must.setImageResource(R.drawable.must_good)
        }
        else if(pm10value in 16.0..30.99){
            binding.mustValue.text = "좋음"
            binding.must.setImageResource(R.drawable.must_normal)
        }
        else if(pm10value in 31.0..79.99){
            binding.mustValue.text="보통"
            binding.must.setImageResource(R.drawable.must_bad)
        }
        else if(pm10value in 81.0..150.99) {
            binding.mustValue.text = "나쁨"
            binding.must.setImageResource(R.drawable.must_deadly)
        }
        binding.rain.setImageResource(R.drawable.rain_80)
        binding.rainValue.text = ((rainValue*10).roundToInt()).div(10f).toString()+"mm"
    }
    private fun setDrawer() {
        val drawerLayout = binding.drawerLayout
        val drawerView = binding.drawerConst

        binding.menu.setOnClickListener {
            if(is_opened==0) {
                drawerLayout.openDrawer(drawerView)
                is_opened=1
            }
            else if(is_opened==1){
                drawerLayout.closeDrawer(drawerView)
                is_opened=0
            }
            if(unitOn==0) {
                binding.unitButton.setImageResource(R.drawable.unit_c_on)
            }
            else if(unitOn==1) {
                binding.unitButton.setImageResource(R.drawable.unit_f_on)
            }
        }
        binding.unitButton.setOnClickListener {
            val intent = Intent(this,LoadingActivity::class.java)
            if(unitOn==0) {
                binding.unitButton.setImageResource(R.drawable.unit_f_on)
                units = "imperial"
                unitOn=1
            }
            else if(unitOn==1){
                binding.unitButton.setImageResource(R.drawable.unit_c_on)
                units = "metric"
                unitOn=0
            }
            is_opened=0

            startActivity(intent)
        }
        binding.interested.setOnClickListener{
            val intent = Intent(this,InterestedActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setBookInfo(){
        //비오는 날이면, 신간전체 리스트 중에서 검색어로 감성을 넣어서 나온 결과. 제목과 표지를 가져온다.
        //비오는 날이 아니면 주목할 만한 신간 리스트인 ItemNewSpecial 을 요청변수에 넣어서 제목과 표지를 가져온다.
//        if(weather_state=="light rain"||weather_state=="heavy rain"||weather_state=="moderate rain"||weather_state=="heavy intensity rain"||weather_state=="thunderstorm with light rain"){
//            binding.tvForvp.text = "비오는 날 추천 도서"
//        }
//        else{

        if(booklist.isNotEmpty()){
            booklist.clear()
        }
        if(description.isNotEmpty()){
            description.clear()
        }

        title = intent.getStringArrayListExtra("title") as ArrayList<String>
        coverSmallUrl = intent.getStringArrayListExtra("coverSmallUrl") as ArrayList<String>
        description = intent.getStringArrayListExtra("description") as ArrayList<String>
        for(i in 0 until description.size){
            if(description[i].length>220)
            description[i] = description[i].substring(0,219)
            description[i] = description[i]+"....더보기"
        }
        CoroutineScope(Dispatchers.Main).launch {
            for (i in 0 until coverSmallUrl.size) {
                val bitmap = withContext(Dispatchers.IO) {
                    ImageLoader.loadImage(coverSmallUrl[i])
                }
                booklist.add(Bookmain(bitmap,title[i],description[i]))
                binding.viewPagerBook.adapter = ViewPagerAdapter(booklist)
                ViewPagerAdapter(booklist).notifyDataSetChanged()
            }
        }
    }
}
//url -> bitmap 이미지
object ImageLoader{
    fun loadImage(imageUrl:String):Bitmap?{
        val bmp:Bitmap? = null
        try{
            val url = URL(imageUrl)
            val stream = url.openStream()
            return BitmapFactory.decodeStream(stream)
        }catch (e:MalformedURLException){
            e.printStackTrace()
        }catch (e:IOException){
            e.printStackTrace()
        }
        return bmp
    }
}