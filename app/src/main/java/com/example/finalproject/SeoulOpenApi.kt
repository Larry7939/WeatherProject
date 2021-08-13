package com.example.finalproject
import android.content.ContentValues.TAG
import android.util.Log
import data.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

var q:String = "Seoul"
private const val appid:String ="85f7e8104e780f9ee552af2b5592e6c8"
var units:String = "metric"
interface WeatherOpenService{
    //http Method 'GET/POST/PUT/DELETE/HEAD 중 어떤 작업인지를 선택
    //value에는 전체 URI에서 URL을 제외한 End Point(URI)
    @GET("weather?")
    fun getWeather(
        @Query("appid")appid:String,
        @Query("q") q: String,
        @Query("units") units:String
        ): Call<Weather> // DATA CLASS //여기 Call<>에는 JSON으로 된 데이터를 받기 위해, 그 데이터에 해당하는 dataclass의 이름을 적어주면 된다.
                     // 존재하는 데이터 클래스 모두를 받아오기 때문에, 꼭!꼭! Sys이렇게 부분 데이터 클래스가 아니라, 전체를 포함하고 있는 Weather로 받아와야한다.
}
//만약 서버 호출이 필요할 때마다 인터페이스를 구현해야 한다면 너무 비효율적이기 때문에
//Client 파일은 싱글톤(Object)으로 제작하는 것이 바람직하다.
object WeatherClinet {
    private const val baseUrl = "http://api.openweathermap.org/data/2.5/" //어떤 서버에 요청을 보낼지 정함. 주소의 끝은 항상 /로 끝남.
    private val retrofit = Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())//데이터를 파싱할 converter를 추가 JSON을 변환해주기 위함.
        .build()
    val service = retrofit.create(WeatherOpenService::class.java)!! //위에서 만든 Retrofit객체로 Interface구현
}
class WeatherRepository {
    fun loadWeatherNotice(mCallback: LoadingActivity){
        //네트워크 작업을 하는 스레드는 비동기로 돌린다.
        val call =WeatherClinet.service.getWeather(appid,q, units)
        //enqueue비동기 방식을 이용해 통신 요청
        //execute동기방식
        call.enqueue(object : Callback<Weather> {
            override fun onResponse( // 통신에 성공한 경우
                call: Call<Weather>, response: Response<Weather>) {
                if(response.isSuccessful){
                    Log.d(TAG, "onResponse: 통신 성공 ${response.body()}")
                    // 응답을 잘 받은 경우 response.body에는 요청한 Main형식의 데이터가 들어온다. 그 안에 있는 temp라는 이름의 데이터 클래스를 가져왔다.
                    // 다만 비동기 실행이므로, LiveData나 콜백함수 기능 등을 이용해서 받아온 응답을 처리해야한다.
                    //메인스레드한테 네트워크처럼 오래걸리는 일을 맡겨버리면, UI를 그리기까지의 딜레이가 너무 크다. 따라서 여기에서 네트워크 작업을 다 하고,
                    //네트워크 작업이 끝나면 콜백을 하는 방식으로. 즉, 비동기적으로 해야한다.
                    mCallback.loadComplete(response.body()!!)
                } else {
                    Log.d(TAG, "onResponse: 응답 오류 ${response.body()}, ${response.code()}")
                    // 통신은 성공했지만 응답에 문제가 있는 경우 ex) 404 Not Fouund
                }
            }
            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.d(TAG, "onResponse: 통신 실패 ")
                // 통신에 실패한 경우
            }
        })
    }
}