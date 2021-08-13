package com.example.finalproject
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.databinding.BookListItemBinding
class ViewPagerAdapter(bookList: ArrayList<Bookmain>) : RecyclerView.Adapter<PagerViewHolder>() {
    var item = bookList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):PagerViewHolder {
        val binding = BookListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PagerViewHolder(binding)
    }
    override fun getItemCount(): Int = item.size
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val intro = item[position].intro
        val iv = item[position].iv
        val desc = item[position].desc
        holder.setView(intro,iv,desc)
    }
}
class PagerViewHolder(private val binding: BookListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setView(intro:String,iv: Bitmap?,desc:String) {
        binding.title.text = intro
        binding.desc.text = desc
        binding.imageViewBook.setImageBitmap(iv)
    }
}
//class ViewPagerAdapter(bookList:ArrayList<Int>):RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
//    var item = bookList
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):PagerViewHolder{
//        val binding = BookListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        return PagerViewHolder(binding,parent)
//    }
//    override fun getItemCount(): Int = item.size
//    override fun onBindViewHolder(holder: ViewPagerAdapter.PagerViewHolder, position: Int) {
//        holder.book.setImageResource(item[position])
//    }
//    inner class PagerViewHolder(binding: BookListItemBinding,parent:ViewGroup):RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_list_item,parent,false)) {
//        val book = itemView.binding.imageViewBook
//    }
//}