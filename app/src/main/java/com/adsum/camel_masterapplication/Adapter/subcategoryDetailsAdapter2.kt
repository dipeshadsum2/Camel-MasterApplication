package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.categoryDetailsResponse3
import com.adsum.camel_masterapplication.databinding.ItemParticipateraceBinding

class subcategoryDetailsAdapter2(var ctx: Context?, val userList: List<categoryDetailsResponse3.Data.Round>, val roundClickListener: OnRoundClickListener, racename: String) : RecyclerView.Adapter<subcategoryDetailsAdapter2.ViewHolder>() {
    private lateinit var itemParticipateraceBinding: ItemParticipateraceBinding
    var ll = racename
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): subcategoryDetailsAdapter2.ViewHolder {
        itemParticipateraceBinding = ItemParticipateraceBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ViewHolder(itemParticipateraceBinding)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: subcategoryDetailsAdapter2.ViewHolder, position: Int) {
        try {
            val camellist = userList[position]
            itemParticipateraceBinding.tvRoundname.text = camellist.round_name
            itemParticipateraceBinding.tvType.text = camellist.type
            itemParticipateraceBinding.tvDistance.text = camellist.distance
            itemParticipateraceBinding.tvPrice.text = camellist.price
            itemParticipateraceBinding.tvCategory.text = camellist.category
            itemParticipateraceBinding.tvNoofparticipant.text = camellist.noofparticipants.toString()
            roundClickListener.let { holder.bind(camellist, position, it,ll) }

//            itemparticipateraceBinding.tvRoundname.text = categorydetails.round_name
//            itemparticipateraceBinding.tvType.text = categorydetails.type
//            itemparticipateraceBinding.tvDistance.text= categorydetails.distance
//            itemparticipateraceBinding.tvPrice.text = categorydetails.price
//            itemparticipateraceBinding.tvCategory.text = categorydetails.category
//            itemparticipateraceBinding.tvNoofparticipant.text= categorydetails.noofparticipants.toString()
            //itemparticipateraceBinding.tvDescription.text = categorydetails.description

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }
    inner class ViewHolder internal constructor(binding: ItemParticipateraceBinding): RecyclerView.ViewHolder(
        binding.root
    ){

        fun bind(camellist: categoryDetailsResponse3.Data.Round, position: Int, clickListener: OnRoundClickListener, racename: String)
        {

            itemParticipateraceBinding.tvRoundname.setOnClickListener {
                clickListener.OnRoundClick(camellist, position,racename)
            }
            itemParticipateraceBinding.tvNoofparticipant.setOnClickListener {
               clickListener.OnParticipateClick(camellist,position,racename)
            }
        }

    }
    interface OnRoundClickListener{
        fun OnRoundClick(camellist: categoryDetailsResponse3.Data.Round, position: Int, racename: String)
        fun OnParticipateClick(
            camellist: categoryDetailsResponse3.Data.Round,
            position: Int,
            racename: String
        )
    }

}