package com.example.finalproject
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.databinding.ItemRecyclerviewBinding
class CustomAdapter() :RecyclerView.Adapter<Holder>(){
    var dataList = ArrayList<Info>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val info = dataList[position]
        holder.setData(info)
        holder.setInterested(info)
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
}
class Holder(val binding:ItemRecyclerviewBinding):RecyclerView.ViewHolder(binding.root){
    fun setData(info:Info){
        binding.itemRecyclerviewTv.text = info.location
    }
    fun setInterested(info:Info) {
        //원래는 어댑터에 있는 바인딩을 여기로 가져와서, Activity에서 제어할 수 있도록 함.
        binding.recycleItem.setOnClickListener {
            val intent = Intent(binding.recycleItem.context,LoadingActivity::class.java)
            ///검색창에 뜬 아이템을 누른 거면, interestedList(관심지역 리스트)에 추가
            //but, 이미 관심지역 리스트에 있는 거라면, 추가하지 않는다.
            if((Info(info.location) in InterestedActivity.displayList)){
                if((Info(info.location) !in InterestedActivity.interestedList)){
                    InterestedActivity.interestedList.add(Info(info.location))
                    Toast.makeText(binding.recycleItem.context, "관심지역으로 추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(binding.recycleItem.context, "이미 관심지역에 추가된 지역입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            //info 리스트에 있는 아이템을 누른거면 지역값 세팅하고 LoadingActivity startActivity는 InterestedActivity에서 하기
            //만약 이렇게 해서 바꾸면, 미세먼지도 바꿔줘야함. 미세먼지는 다른 api니까.
            else if(Info(info.location) in InterestedActivity.infoList){
                when (info.location) {
                    "서울 특별시" -> { q = "Seoul"
                        lon=126.9778
                        lat=37.5683
                    }
                    "인천 광역시" -> { q = "Incheon"
                        lon=126.4161
                        lat=37.46
                    }
                    "대전 광역시" -> { q = "Daejeon"
                        lon=127.4167
                        lat=36.3333
                    }
                    "세종특별자치시" -> {q="Sejong"
                        lon=127.2871
                        lat=36.4817
                    }
                    "광주 광역시" -> { q= "Gwangju"
                        lon=126.9156
                        lat=35.1547
                    }
                    "대구 광역시" -> { q="Daegu"
                        lon=128.55
                        lat=35.8
                    }
                    "부산 광역시" -> { q="Busan"
                        lon=129.0403
                        lat=35.1028}
                    "울산 광역시" -> { q="Ulsan"
                        lon=129.3167
                        lat=35.5372 }
                    "제주특별자치도 제주시" -> { q="Jeju"
                        lon=126.5219
                        lat=33.5091}
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                binding.recycleItem.context.startActivity(intent)
            }
        }
    }

}