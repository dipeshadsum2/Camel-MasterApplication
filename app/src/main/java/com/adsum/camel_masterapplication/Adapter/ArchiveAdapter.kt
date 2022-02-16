package com.adsum.camel_masterapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Adapter.ArchiveAdapter.ViewHolder
import com.adsum.camel_masterapplication.Model.ArchiveData
import com.adsum.camel_masterapplication.R

class ArchiveAdapter(val userList: List<ArchiveData.Data>, val clickListener: OnItemClickListener) : RecyclerView.Adapter<ViewHolder>() {
//    private lateinit var itemMainRvBinding: ItemHistoryBinding
//    private val viewPool = RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        itemMainRvBinding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(itemMainRvBinding)
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(itemView)
    }
    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val date = userList[position]
            holder.tvYear.text = date.month.toString() + "  " +date.year.toString()


            clickListener.let {
                holder.bind(date,position,clickListener,date.month,date.year)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }
    inner class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(
        itemView
    ){
        val tvYear : TextView = itemView.findViewById(R.id.tv_year)
        fun bind(camellist: ArchiveData.Data, position: Int, clickListener: OnItemClickListener, month: Int, year: Int)
        {
            tvYear.setOnClickListener {
                clickListener.OnCustomClick(camellist, position, month, year)
            }
        }
    }
    interface OnItemClickListener{
        fun OnCustomClick(history: ArchiveData.Data, position: Int, month: Int, year: Int)
    }
}
