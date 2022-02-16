package com.adsum.camel_masterapplication.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.RaceDetailResponse

import com.adsum.camel_masterapplication.databinding.ItemRaceDetailBinding
import java.lang.Exception


class RaceDetailAdapter(
    var ctx: Context,
    var raceList: ArrayList<RaceDetailResponse.Data>,
    val raceDetailClickListener: OnRaceDetailClickListener
    ) : RecyclerView.Adapter<RaceDetailAdapter.ViewHolder>() {

    private lateinit var itemRaceDetailBinding:ItemRaceDetailBinding
    private lateinit var items:ArrayList<RaceDetailResponse.Data>



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemRaceDetailBinding=ItemRaceDetailBinding.inflate(LayoutInflater.from(ctx),parent,false)
        return ViewHolder(itemRaceDetailBinding)
//        var raceDetail = LayoutInflater.from(parent.context).inflate(R.layout.item_race_detail,parent,false)
//        return ViewHolder(raceDetail)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       try{
           val raceList =  raceList[position]
           itemRaceDetailBinding.tvRaceName.text=raceList.raceName
           itemRaceDetailBinding.tvRaceId.text=raceList.raceId
           itemRaceDetailBinding.tvNoOfround.text=raceList.noOfRound
           itemRaceDetailBinding.tvStartdate.text=raceList.startDate
           itemRaceDetailBinding.tvEndDate.text=raceList.endDate

           raceDetailClickListener.let { holder.bind(raceList,position,it) }



       }catch (e:Exception){
           e.printStackTrace()
       }
    }

    override fun getItemCount(): Int {
        return raceList.size
    }




   inner class ViewHolder internal constructor(binding: ItemRaceDetailBinding): RecyclerView.ViewHolder(
       binding.root

    ) {

       fun bind(
           raceList: RaceDetailResponse.Data, position: Int, clickListener: OnRaceDetailClickListener
       ){
            itemRaceDetailBinding.tvRaceName.setOnClickListener {
               clickListener.OnRaceDetailClickListener(raceList, position)
           }
           itemRaceDetailBinding.imgDelete.setOnClickListener{
               clickListener.OnDeleteRecord(raceList,position)
           }
           itemRaceDetailBinding.imgSchedule.setOnClickListener{
               clickListener.OnRaceScheduleClickListener(raceList,position,raceList.raceId)
           }
           itemRaceDetailBinding.imgUsers.setOnClickListener{
               clickListener.OnUserClickListener(raceList,position)
           }
       }
   }

    @SuppressLint("NotifyDataSetChanged")
    fun deleterace(position: Int){
        raceList.removeAt(position)
       // notifyItemRemoved(position)
       notifyDataSetChanged()
    }
//    fun update(modelList:ArrayList<RaceDetailResponse.Data>){
//        raceList = modelList
//        notifyDataSetChanged()
//    }






   @SuppressLint("NotifyDataSetChanged")
//   fun updateRaceDate(position: String)
//   {
//       raceList.get(position).startDate="24-2-2022"
//       raceList.get(position).endDate="24-2-2022"
//       notifyDataSetChanged()
//   }
    interface OnRaceDetailClickListener{
        fun OnRaceDetailClickListener(raceList: RaceDetailResponse.Data, position: Int)
        fun OnDeleteRecord(raceList: RaceDetailResponse.Data, position: Int)
        fun OnRaceScheduleClickListener(
            raceList: RaceDetailResponse.Data, position: Int,
            raceid: String
        )
        fun OnUserClickListener(raceList: RaceDetailResponse.Data, position: Int)

    }


}


