package com.example.finalproject
import android.content.ContentValues
import android.util.Log
import data.OpenWeather_fm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

var lat = 37.5683
var lon = 126.9778
private val appid = "85f7e8104e780f9ee552af2b5592e6c8"
interface FMmeasureService{
    @GET("forecast?")
    fun getFMInfo(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") appid:String
        ): Call<OpenWeather_fm>
}
object FMclient{
    private const val baseUrl = "http://api.openweathermap.org/data/2.5/air_pollution/" //어떤 서버에 요청을 보낼지 정함. 주소의 끝은 항상 /로 끝남.
    private val retrofit = Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())//데이터를 파싱할 converter를 추가 JSON을 변환해주기 위함.
        .build()
    val service = retrofit.create(FMmeasureService::class.java)!! //위에서 만든 Retrofit객체로 Interface구현
}
class FMrepositary{
    fun loadFMNotice(mCallback: LoadingActivity){
        //네트워크 작업을 하는 스레드는 비동기로 돌린다.
        val call =FMclient.service.getFMInfo(
            lat,
            lon,
            appid
        )
        //enqueue비동기 방식을 이용해 통신 요청
        //execute동기방식
        call.enqueue(object : Callback<OpenWeather_fm> {
            override fun onResponse( // 통신에 성공한 경우
                call:Call<OpenWeather_fm>, response: Response<OpenWeather_fm>) {
                if(response.isSuccessful){
                    Log.d(ContentValues.TAG, "onResponse: 미세먼지 통신 성공 ${response.body()}")
                    // 응답을 잘 받은 경우 response.body에는 요청한 Main형식의 데이터가 들어온다. 그 안에 있는 temp라는 이름의 데이터 클래스를 가져왔다.
                    // 다만 비동기 실행이므로, LiveData나 콜백함수 기능 등을 이용해서 받아온 응답을 처리해야한다.
                    //메인스레드한테 네트워크처럼 오래걸리는 일을 맡겨버리면, UI를 그리기까지의 딜레이가 너무 크다. 따라서 여기에서 네트워크 작업을 다 하고,
                    //네트워크 작업이 끝나면 콜백을 하는 방식으로. 즉, 비동기적으로 해야한다.
                    mCallback.loadComplete2(response.body()!!)
                } else {
                    Log.d(ContentValues.TAG, "onResponse: 응답 오류 ${response.body()}, ${response.code()}")
                    // 통신은 성공했지만 응답에 문제가 있는 경우 ex) 404 Not Fouund
                }
            }
            override fun onFailure(call: retrofit2.Call<OpenWeather_fm>, t: Throwable) {
                Log.d(ContentValues.TAG, "onResponse: 미세먼지 통신 실패${t.cause.toString()} ${t.message.toString()}")
                // 통신에 실패한 경우
            }
        })
    }
}