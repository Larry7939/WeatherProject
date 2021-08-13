package com.example.finalproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.databinding.ActivityInterestedBinding
import com.example.finalproject.databinding.ItemRecyclerviewBinding

//1.반복되는 틀(아이템 레이아웃
//2. 데이터(ArrayList or mutableList)
//3. 어댑터 클래스
data class Info(val location:String)


class InterestedActivity : AppCompatActivity() {

    companion object {
        val infoList = ArrayList<Info>()//모든 지역 정보 리스트 //검색하면 이 중 일부가 전시됨.
        val displayList = ArrayList<Info>()//검색 시 보여주기용 리스트
        val interestedList = ArrayList<Info>()//관심 지역 정보 리스트
    }
    private lateinit var binding:ActivityInterestedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterestedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(infoList.isEmpty()) {
            addInfo()
        }
        refreshRecyclerView()
        search()
        binding.backButton.setOnClickListener{
            displayList.clear()
            finish()
        }
    }
   override fun onBackPressed() {
        super.onBackPressed()
       displayList.clear()
        finish()
    }
    //정보 추가 함수
    private fun addInfo(){
        infoList.add(Info("서울특별시"))
        infoList.add(Info("인천 광역시"))
        infoList.add(Info("대전 광역시"))
        infoList.add(Info("세종특별자치시"))///
        infoList.add(Info("광주 광역시"))
        infoList.add(Info("대구 광역시"))
        infoList.add(Info("부산 광역시"))
        infoList.add(Info("울산 광역시"))
        infoList.add(Info("제주특별자치도 제주시"))
    }
    //연결용 함수(리사이클러뷰&어댑터 , 액티비티의 infoList&어댑터의 dataList
    private fun refreshRecyclerView(){
        val adapter = CustomAdapter()
        adapter.dataList= interestedList
        binding.recyclerviewInterested.adapter=adapter //연결
        binding.recyclerviewInterested.layoutManager = LinearLayoutManager(this) //연결
    }
    //검색용 함수
    private fun search(){
        binding.searchviewLocation.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                var searchText:String = binding.searchviewLocation.text.toString()
                //어댑터에 정의해놓은 fillter에 searchText를 집어넣음.
                fillter(searchText)
            }
        })
    }
    private fun fillter(searchText:String){
        //매번 입력할 때마다 clear해준다.
        displayList.clear()
        //검색창이 비면, 관심지역으로 지정한 리스트를 화면에 띄운다.
        if(searchText.isEmpty()){
            val adapter = CustomAdapter()
            adapter.dataList= interestedList
            binding.recyclerviewInterested.adapter=adapter //연결
            binding.recyclerviewInterested.layoutManager = LinearLayoutManager(this) //연결
        }
        else{
            //실제 데이터가 들어있는 infoList를 뒤져서 searchText를 포함한 item을 검색 시 보여주기용 리스트인 displayListItem에 add
            for(i in 0 until infoList.size){
                if(infoList[i].location.contains(searchText)){
                    displayList.add(infoList[i])
                    val adapter = CustomAdapter()
                    adapter.dataList= displayList
                    binding.recyclerviewInterested.adapter=adapter //연결
                    binding.recyclerviewInterested.layoutManager = LinearLayoutManager(this) //연결
                }
            }
        }
    }

}